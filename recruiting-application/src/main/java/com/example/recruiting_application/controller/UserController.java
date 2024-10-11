    package com.example.recruiting_application.controller;


    import com.example.recruiting_application.model.JobApplication;
    import com.example.recruiting_application.model.Jobs;

    import com.example.recruiting_application.model.User;
//    import com.example.recruiting_application.repository.SimRepo;
    import com.example.recruiting_application.repository.JobApplicationRepo;
    import com.example.recruiting_application.repository.JobRepo;
    import com.example.recruiting_application.repository.UserRepo;

    import com.example.recruiting_application.service.JobApplicationService;
    import com.example.recruiting_application.service.JobService;
//    import com.example.recruiting_application.service.ProfileService;
    import com.example.recruiting_application.service.UserService;


    import org.springframework.format.annotation.DateTimeFormat;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;
    import org.springframework.web.multipart.MultipartFile;

    import java.io.IOException;
    import java.time.LocalDate;
    import java.util.*;


    @RestController
    @RequestMapping("/api")
    public class UserController {


        private UserService userService;
        private UserRepo userRepo;
        private final JobService jobService;
        private JobApplicationService jobApplicationService;
        private JobRepo jobRepo;


        public UserController(UserService userService, JobService jobService, JobApplicationService jobApplicationService, UserRepo userRepo, JobRepo jobRepo) {
            this.userService = userService;
            this.jobService = jobService;
            this.jobApplicationService = jobApplicationService;
            this.userRepo = userRepo;
            this.jobRepo = jobRepo;
        }


        @PostMapping("/login")
        public ResponseEntity<Map<String, Object>> login(@RequestParam String email, @RequestParam String password) {
            boolean valid = userService.validateUser(email, password);
            Map<String, Object> response = new HashMap<>();

            if (valid) {
                // Fetch userId after successful validation
                Long userId = userService.getUserIdByEmail(email); // Assuming you have this method
                response.put("success", true);
                response.put("userId", userId); // Include userId in the response
                response.put("message", "Login successful");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Invalid credentials");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        }


        @PostMapping("/register")
        public @ResponseBody ResponseEntity<Map<String, Object>> register(@RequestBody User user) {
            Map<String, Object> response = new HashMap<>();
            try {
                User newUser = userService.registerUser(user);
                response.put("success", true);
                response.put("message", "User registered successfully.");
                return ResponseEntity.ok(response);
            } catch (IllegalArgumentException e) {
                response.put("success", false);
                response.put("message", e.getMessage());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            } catch (Exception e) {
                response.put("success", false);
                response.put("message", "An error occurred: " + e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        }


        @PostMapping("/createJob")
        public ResponseEntity<Map<String, Object>> createJob(@RequestBody Jobs job) {
            Map<String, Object> response = new HashMap<>();
            try {
                Jobs savedJob = jobService.saveJob(job);
                response.put("success", true);
                response.put("message", "Job created successfully.");
                response.put("job", savedJob);
                return ResponseEntity.ok(response);  // Returns status 200 OK with response
            } catch (Exception e) {
                response.put("success", false);
                response.put("message", "An error occurred: " + e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        }

        @GetMapping("/search")
        public List<Jobs> searchJobsByTitle(@RequestParam String jobTitle) {
            return jobRepo.findByJobTitleContainingIgnoreCaseAndStatus(jobTitle, "publish");
        }

        @GetMapping("/drafts")
        public List<Jobs> getAllDrafts() {
            return jobService.getDrafts();
        }

        // to get the published jobs

        @GetMapping("/published")
        public List<Jobs> getAllPublishedJobs() {
            return jobService.getPublished();
        }

        @GetMapping("/profile/{email}")
        public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
            User user = userService.findUserByEmail(email);
            if (user != null) {
                return ResponseEntity.ok(user);
            } else {
                return ResponseEntity.notFound().build(); // Return 404 if user not found
            }
        }

        // to update  the profile by UserId

        @PutMapping("/profile/update/{id}")
        public ResponseEntity<String> updateUserProfile(@PathVariable Long id, @RequestBody User updatedUser) {
            boolean isUpdated = userService.updateUser(id, updatedUser);
            if (isUpdated) {
                return ResponseEntity.ok("Profile Updated Successfully");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error Updating the Profile");
            }
        }

        // to update the drafts
        @PutMapping("/updateJob/{id}")
        public ResponseEntity<Map<String, Object>> updateJob(@PathVariable Long id, @RequestBody Jobs updatedJob) {
            Map<String, Object> response = new HashMap<>();
            try {
                Jobs job = jobService.updateJob(id, updatedJob);
                response.put("success", true);
                response.put("message", "Job updated successfully.");
                response.put("job", job);
                return ResponseEntity.ok(response);
            } catch (RuntimeException e) {
                response.put("success", false);
                response.put("message", e.getMessage());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            } catch (Exception e) {
                response.put("success", false);
                response.put("message", "An error occurred: " + e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        }

        // to delete the drafts post
        @DeleteMapping("/deleteJob/{id}")
        public ResponseEntity<String> deleteJob(@PathVariable Long id) {
            boolean isDeleted = jobService.deleteJob(id);
            if (isDeleted) {
                return ResponseEntity.ok("Job deleted Successfully");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Job not found");
            }
        }

        // to view the full job details

        @GetMapping("/job/{id}")
        public ResponseEntity<Jobs> getJobById(@PathVariable Long id) {
            Jobs jobs = jobService.findById(id);
            if (jobs == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(jobs, HttpStatus.OK);
        }

    }