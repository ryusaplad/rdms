package svfc_rdms.rdms.controller.RestControllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import svfc_rdms.rdms.ExceptionHandler.ApiRequestException;
import svfc_rdms.rdms.model.Users;
import svfc_rdms.rdms.repository.Global.UsersRepository;
import svfc_rdms.rdms.serviceImpl.Facilitator_Registrar.Facilitator_Registrar_ServiceImpl;
import svfc_rdms.rdms.serviceImpl.Student.StudentServiceImpl;

@RestController
public class Faci_Regs_RestController {

     @Autowired
     StudentServiceImpl studentServiceImpl;

     @Autowired
     UsersRepository userRepository;

     @Autowired
     Facilitator_Registrar_ServiceImpl faci_regs_ServiceImpl;

     @GetMapping("/{userType}/studentrequest/fetch")
     public ResponseEntity<Object> getRequestInformation(@PathVariable String userType, @RequestParam("s") Long userId,
               @RequestParam("req") Long requestId,
               HttpServletResponse response, HttpSession session) {
          if (userType.isEmpty() || userType.isBlank()) {
               throw new ApiRequestException("Invalid User.");
          } else {
               Optional<Users> user = userRepository.findByuserId(userId);
               if (user.isPresent()) {
                    Users getUser = user.get();
                    String username = getUser.getUsername();
                    return studentServiceImpl.fetchRequestInformationToModals(username, requestId);
               } else {
                    throw new ApiRequestException("Can't get the users.");
               }
          }

     }

     @GetMapping(value = "/{userType}/studentreq/change/{status}")
     @ResponseBody
     public ResponseEntity<Object> changeRequestInformations(@PathVariable String userType,
               @PathVariable("status") String status,
               @RequestParam("userId") long userId, @RequestParam("requestId") long requestId,
               @RequestParam("reason") String message,
               HttpServletResponse response,
               HttpSession session) {
          List<String> validUserType = new ArrayList<>();
          validUserType.add("facilitator");
          validUserType.add("registrar");
          if (userType.isEmpty() || userType.isBlank() || !validUserType.contains(userType)) {
               throw new ApiRequestException("Invalid User.");
          } else {
               if (!status.isEmpty() || !status.isBlank() || session.getAttribute("name") != null) {
                    String manageBy = session.getAttribute("name").toString();

                    // changing status based on the input
                    if (faci_regs_ServiceImpl.changeStatusAndManageByAndMessageOfRequests(status, manageBy, message,
                              userId, requestId)) {
                         return new ResponseEntity<>("Success", HttpStatus.OK);
                    } else {
                         throw new ApiRequestException("Failed to change requests status.");
                    }
               }
          }

          return new ResponseEntity<>("failed to change status, invalid data.", HttpStatus.BAD_REQUEST);
     }

     @PostMapping("/registrar/studentreq/finalized")
     public ResponseEntity<Object> finalizedRequest(@RequestParam long userId, @RequestParam long requestId,
               @RequestParam("rfile[]") Optional<MultipartFile[]> files,
               @RequestParam Map<String, String> params, HttpSession session) {

          return faci_regs_ServiceImpl.finalizedRequestsWithFiles(userId, requestId, files, params, session);

     }
}
