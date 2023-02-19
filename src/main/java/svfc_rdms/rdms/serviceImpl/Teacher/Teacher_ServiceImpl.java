package svfc_rdms.rdms.serviceImpl.Teacher;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import svfc_rdms.rdms.ExceptionHandler.ApiRequestException;
import svfc_rdms.rdms.dto.RegistrarRequest_DTO;
import svfc_rdms.rdms.model.RegistrarRequest;
import svfc_rdms.rdms.model.StudentRequest;
import svfc_rdms.rdms.model.UserFiles;
import svfc_rdms.rdms.model.Users;
import svfc_rdms.rdms.repository.File.FileRepository;
import svfc_rdms.rdms.repository.Global.UsersRepository;
import svfc_rdms.rdms.repository.RegistrarRequests.RegRepository;
import svfc_rdms.rdms.service.File.FileService;
import svfc_rdms.rdms.service.Teacher.Teacher_Service;
import svfc_rdms.rdms.serviceImpl.Global.GlobalServiceControllerImpl;

@Service
public class Teacher_ServiceImpl implements Teacher_Service, FileService {

     @Autowired
     private UsersRepository usersRepository;

     @Autowired
     private FileRepository fileRepository;

     @Autowired
     private RegRepository regsRepository;

     @Autowired
     private GlobalServiceControllerImpl globalService;

     @Override
     public Optional<RegistrarRequest> getRegistrarRequest(long requestsId) {

          Optional<RegistrarRequest> registrarRequests = regsRepository.findOneByRequestId(requestsId);
          if (requestsId > -1 && !registrarRequests.isEmpty()) {
               return registrarRequests;
          }
          return Optional.empty();
     }

     @Override
     public ResponseEntity<Object> viewRegistrarRequests(long requestsId) {
          Optional<RegistrarRequest> req = getRegistrarRequest(requestsId);
          List<RegistrarRequest_DTO> registrarDtoCompressor = new ArrayList<>();

          if (req.isPresent()) {
               List<UserFiles> regRequestFiles = fileRepository.findAllByRegRequestsWith(req.get());
               RegistrarRequest regReq = req.get();
               RegistrarRequest_DTO regDto = new RegistrarRequest_DTO(
                         regReq.getRequestId(),
                         regReq.getRequestTitle(), regReq.getRequestMessage(), regReq.getTeacherMessage(),
                         regReq.getRequestBy().getName(),
                         regReq.getRequestTo().getName(), regReq.getRequestDate(),
                         regReq.getDateOfUpdate(), regReq.getRequestStatus());
               registrarDtoCompressor.add(regDto);

               if (regRequestFiles != null) {
                    regRequestFiles.forEach(userFiles -> {

                         registrarDtoCompressor.add(new RegistrarRequest_DTO(
                                   userFiles.getFileId(), userFiles.getName(),
                                   userFiles.getSize(),
                                   userFiles.getStatus(), userFiles.getDateUploaded(),
                                   userFiles.getFilePurpose(),
                                   userFiles.getUploadedBy().getName()));
                    });

               }
               return new ResponseEntity<Object>(registrarDtoCompressor, HttpStatus.OK);

          } else {
               throw new ApiRequestException("No data found for the given requests ID");
          }
     }

     @Override
     public ResponseEntity<String> sendRequestToRegistrar(long requestsId, HttpSession session,
               Optional<MultipartFile[]> files,
               Map<String, String> params) {
          List<String> excludedFiles = new ArrayList<>();
          Optional<RegistrarRequest> optionalRegRequest = getRegistrarRequest(requestsId);

          String username = (String) session.getAttribute("username");
          String message = "";
          boolean messageAvailability = false;
          boolean filesAvailability = false;
          if (username == null || username.isEmpty()) {
               throw new ApiRequestException("Can't send requests. Please contact the administrator!");
          }

          Optional<Users> optionalUser = usersRepository.findByUsername(username);

          if (optionalRegRequest.isPresent() && optionalUser.isPresent()) {
               try {
                    Users uploadedBy = optionalUser.get();
                    RegistrarRequest registrarRequest = optionalRegRequest.get();

                    for (Map.Entry<String, String> entry : params.entrySet()) {
                         String key = entry.getKey().replace(" ", "");
                         if (key.contains("excluded")) {
                              excludedFiles.add(entry.getValue());
                         } else if (key.contains("message")) {
                              message = entry.getValue();
                              registrarRequest.setTeacherMessage(message);
                              messageAvailability = true;
                         }
                    }

                    if (files.isPresent()) {
                         for (MultipartFile file : files.get()) {
                              if (!excludedFiles.contains(file.getOriginalFilename())) {
                                   UserFiles userFile = new UserFiles();
                                   userFile.setData(file.getBytes());
                                   userFile.setName(file.getOriginalFilename());
                                   userFile.setSize(globalService.formatFileUploadSize(file.getSize()));
                                   userFile.setDateUploaded(globalService.formattedDate());
                                   userFile.setStatus("Approved");
                                   userFile.setFilePurpose("student-record");
                                   userFile.setUploadedBy(uploadedBy);
                                   userFile.setRegRequestsWith(registrarRequest);
                                   fileRepository.save(userFile);
                              }
                         }
                         filesAvailability = true;
                    } else {
                         registrarRequest.setRequestStatus("norecord");
                    }
                    
                    if (messageAvailability && filesAvailability) {
                         registrarRequest.setRequestStatus("completed");
                    } else if (messageAvailability) {
                         registrarRequest.setRequestStatus("messageonly");
                    } else if (filesAvailability) {
                         registrarRequest.setRequestStatus("recordonly");
                    }
                    
                    regsRepository.save(registrarRequest);
                    return new ResponseEntity<>("Success", HttpStatus.OK);
               } catch (Exception e) {
                    throw new ApiRequestException("Can't send requests. Please contact the administrator!");
               }
          }
          throw new ApiRequestException("Can't send requests. Please try again later!");
     }

     @Override
     public String displayAllRequests(HttpSession session, Model model) {
          List<RegistrarRequest_DTO> filteredRequests = new ArrayList<>();
          try {

               String username = session.getAttribute("username").toString();
               if (!username.isBlank() || !username.isEmpty() || username.length() > -1) {
                    Optional<Users> user = usersRepository.findByUsername(username);

                    if (user.isPresent()) {
                         List<RegistrarRequest> regRequests = regsRepository.findAllByRequestTo(user.get());
                         if (regRequests != null) {
                              regRequests.forEach(request -> {

                                   filteredRequests
                                             .add(new RegistrarRequest_DTO(request.getRequestId(),
                                                       request.getRequestTitle(),
                                                       request.getRequestMessage(), request.getTeacherMessage(),
                                                       request.getRequestBy().getName(),
                                                       request.getRequestTo().getName(), request.getRequestDate(),
                                                       request.getDateOfUpdate(),
                                                       request.getRequestStatus()));

                              });

                              model.addAttribute("requests_list", filteredRequests);
                              return "/teacher/requests";
                         }
                    }

               }
          } catch (Exception e) {
               return "/teacher/teach";
          }
          model.addAttribute("requests_list", filteredRequests);
          return "/teacher/requests";
     }

     @Override
     public boolean saveFiles(UserFiles files) {

          return false;
     }

     @Override
     public List<UserFiles> getAllFiles() {

          return null;
     }

     @Override
     public Optional<UserFiles> getFileById(String id) {

          return Optional.empty();
     }

     @Override
     public List<UserFiles> getFilesByRequestWith(StudentRequest sr) {

          return null;
     }

     @Override
     public List<UserFiles> getAllFilesByUser(long userId) {

          return null;
     }

     @Override
     public Boolean deleteFile(long id) {

          return null;

     }

}
