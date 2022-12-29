package svfc_rdms.rdms.controller.RestControllers;

import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import svfc_rdms.rdms.ExceptionHandler.ApiRequestException;
import svfc_rdms.rdms.model.Users;
import svfc_rdms.rdms.repository.Global.UsersRepository;
import svfc_rdms.rdms.serviceImpl.Facilitator.FacilitatorServiceImpl;
import svfc_rdms.rdms.serviceImpl.Student.StudentServiceImpl;

@RestController
public class Facilitator_RestController {

     @Autowired
     StudentServiceImpl studentServiceImpl;

     @Autowired
     UsersRepository userRepository;

     @Autowired
     FacilitatorServiceImpl facilitatorServiceImpl;

     @GetMapping("/facilitator/studentrequest/fetch")
     public ResponseEntity<Object> getRequestInformation(@RequestParam("s") Long userId,
               @RequestParam("req") Long requestId,
               HttpServletResponse response, HttpSession session) {
          Optional<Users> user = userRepository.findByuserId(userId);
          if (user.isPresent()) {
               Users getUser = user.get();
               String username = getUser.getUsername();
               return studentServiceImpl.fetchRequestInformationToModals(username, requestId);
          } else {
               throw new ApiRequestException("Can't get the users.");
          }

     }

     @GetMapping(value = "/facilitator/studentreq/change/{status}")
     @ResponseBody
     public ResponseEntity<Object> changeRequestInformations(@PathVariable("status") String status,
               @RequestParam("userId") long userId, @RequestParam("reason") String message,
               HttpServletResponse response,
               HttpSession session) {

          if (!status.isEmpty() || !status.isBlank() || session.getAttribute("name") != null) {
               String manageBy = session.getAttribute("name").toString();

               // changing status based on the input
               if (facilitatorServiceImpl.changeStatusAndManageByAndMessageOfRequests(status, manageBy, message,
                         userId)) {
                    return new ResponseEntity<>("Success", HttpStatus.OK);
               } else {
                    throw new ApiRequestException("Failed to change requests status.");
               }
          }

          return new ResponseEntity<>("failed", HttpStatus.BAD_REQUEST);
     }
}
