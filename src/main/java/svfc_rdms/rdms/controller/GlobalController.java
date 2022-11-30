package svfc_rdms.rdms.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class GlobalController {

     @GetMapping(value = "/")
     public String loginPage() {

          return "/index";
     }

     // select request page
     @GetMapping(value = "/request/{document}")
     public String requestForm(@PathVariable String document, Model model) {
          model.addAttribute("documentType", document);
          return "/request-form";
     }

     @PostMapping(value = "/request/{document}/sent")
     public String sentRequest(@PathVariable String document, @RequestParam Map<String, String> params, Model model) {
          System.out.println("Requesting in : " + document);
          for (Map.Entry<String, String> entry : params.entrySet()) {
               System.out.println("Key : " + entry.getKey() + " Value : " + entry.getValue());
          }
          return "/request-form";
     }

     
}
