package org.ably.circular.enterprise;


import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
@Tag(name = "Auth Controller", description = "Authentication APIs")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class EnterpriseController {
}
