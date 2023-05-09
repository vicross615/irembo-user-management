package com.irembo.useraccountmanagement.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.irembo.useraccountmanagement.models.ERole;
import com.irembo.useraccountmanagement.models.Role;
import com.irembo.useraccountmanagement.models.User;
import com.irembo.useraccountmanagement.payload.request.LoginRequest;
import com.irembo.useraccountmanagement.payload.request.SignupRequest;
import com.irembo.useraccountmanagement.payload.response.JwtResponse;
import com.irembo.useraccountmanagement.payload.response.MessageResponse;
import com.irembo.useraccountmanagement.repository.RoleRepository;
import com.irembo.useraccountmanagement.repository.UserRepository;
import com.irembo.useraccountmanagement.security.jwt.JwtUtils;
import com.irembo.useraccountmanagement.security.services.UserDetailsImpl;
import com.irembo.useraccountmanagement.service.MfaService;
import com.irembo.useraccountmanagement.service.PasswordlessService;
import com.irembo.useraccountmanagement.service.SessionService;
import com.irembo.useraccountmanagement.service.UserService;
import com.irembo.useraccountmanagement.util.PasswordValidatorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;

	@Autowired
	UserService userService;

	@Autowired
	private SessionService sessionService;


	@Autowired
	private MfaService mfaService;

	@Autowired
	private PasswordlessService passwordlessService;

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
		if (authentication != null) {
			SecurityContextHolder.getContext().setAuthentication(authentication);
			String jwt = jwtUtils.generateJwtToken(authentication);

			HttpHeaders headers = new HttpHeaders();
			String sessionId = sessionService.createSession(loginRequest.getUsername(), jwt);
			headers.add("Set-Cookie", "sessionId=" + sessionId + "; HttpOnly; SameSite=Lax");

			UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
			List<String> roles = userDetails.getAuthorities().stream()
					.map(item -> item.getAuthority())
					.collect(Collectors.toList());

			if (userDetails != null && userDetails.isMfaEnabled()) {
				mfaService.generateAndSendMfaCode(userDetails.getEmail());
				return new ResponseEntity<>("MFA code sent, please verify", HttpStatus.OK);
			} else {

				headers.add("Authorization", "Bearer " + jwt);
				return new ResponseEntity<>("Logged in", headers, HttpStatus.OK);
			}
		} else {
			return new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED);
		}
	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
		String validationResult = PasswordValidatorUtil.validatePassword(signUpRequest.getPassword());
		if (validationResult != null) {
			return new ResponseEntity<>("Invalid password: " + validationResult, HttpStatus.BAD_REQUEST);
		}
		if (userRepository.existsByUsername(signUpRequest.getUsername())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Username is already taken!"));
		}

		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Email is already in use!"));
		}

		// Create new user's account
		User user = new User(signUpRequest.getUsername(), 
							 signUpRequest.getEmail(),
							 encoder.encode(signUpRequest.getPassword()), signUpRequest.isMfaEnabled());

		Set<String> strRoles = signUpRequest.getRole();
		Set<Role> roles = new HashSet<>();

		if (strRoles == null) {
			Role userRole = roleRepository.findByName(ERole.ROLE_USER)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roles.add(userRole);
		} else {
			strRoles.forEach(role -> {
				switch (role) {
				case "admin":
					Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(adminRole);

					break;
				case "mod":
					Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(modRole);

					break;
				default:
					Role userRole = roleRepository.findByName(ERole.ROLE_USER)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(userRole);
				}
			});
		}

		user.setRoles(roles);
		userRepository.save(user);

		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	}

	@PostMapping("/reset-password")
	public ResponseEntity<String> resetPassword(@RequestParam("email") String email,
												@RequestParam("newPassword") String newPassword) {
		User user = userService.findByEmail(email);
		if (user != null) {
			userService.changePassword(email, newPassword);
			return new ResponseEntity<>("Password reset successfully", HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@GetMapping("/logout")
	public ResponseEntity<String> logoutUser(HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			new SecurityContextLogoutHandler().logout(request, response, auth);
			return new ResponseEntity<>("Logged out successfully", HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	// Add a new API endpoint for sending the MFA code after a successful login
//	@PostMapping("/send-mfa-code")
//	public ResponseEntity<String> sendMfaCode(@RequestBody String email) {
//		User existingUser = userService.findByEmail(email);
//		if (existingUser != null) {
//			mfaService.generateAndSendMfaCode(existingUser.getEmail());
//			return new ResponseEntity<>("MFA code sent", HttpStatus.OK);
//		} else {
//			return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
//		}
//	}

	// Add a new API endpoint for verifying the MFA code
	@PostMapping("/verify-mfa-code")
	public ResponseEntity<String> verifyMfaCode(@RequestParam("email")  String email, @RequestParam("code") String code, @CookieValue("sessionId") String sessionId) {
		User existingUser = userService.findByEmail(email);
		if (existingUser != null) {
			if (mfaService.verifyMfaCode(sessionId, code)) {
				String accessToken = sessionService.getAccessToken(sessionId);
				HttpHeaders headers = new HttpHeaders();
				headers.add("Authorization", "Bearer " + accessToken);
				return new ResponseEntity<>("MFA code verified", headers, HttpStatus.OK);
			} else {
				return new ResponseEntity<>("Invalid MFA code", HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
		}
	}

	// Add a new API endpoint for generating and sending a passwordless login link
	@PostMapping("/send-login-link")
	public ResponseEntity<String> sendLoginLink(@RequestParam("email") String email) {
		User existingUser = userService.findByEmail(email);
		if (existingUser != null) {
			String loginLink = passwordlessService.generateLoginLink(existingUser);
			// Send the login link to the user, e.g., via email
			return new ResponseEntity<>("Login link sent", HttpStatus.OK);
		} else {
			return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
		}
	}

	// Add a new API endpoint for generating and sending a passwordless reset link
	@PostMapping("/send-reset-link")
	public ResponseEntity<String> sendResetLink(@RequestParam("email") String email) {
		User existingUser = userService.findByEmail(email);
		if (existingUser != null) {
			String resetLink = passwordlessService.generateResetLink(existingUser);
			// Send the reset link to the user, e.g., via email
			return new ResponseEntity<>("Reset link sent", HttpStatus.OK);
		} else {
			return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
		}
	}

	// Add a new API endpoint for processing the passwordless login link
	@GetMapping("/process-login-link/{token}")
	public ResponseEntity<String> processLoginLink(@PathVariable("token") String token) {
		User user = passwordlessService.verifyLoginLink(token);
		if (user != null) {
			// Log the user in and redirect them to the appropriate page
			return new ResponseEntity<>("Login successful", HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Invalid login link", HttpStatus.BAD_REQUEST);
		}
	}
}
