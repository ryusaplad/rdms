package svfc_rdms.rdms.serviceImpl.Global;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import svfc_rdms.rdms.Enums.College_Courses;
import svfc_rdms.rdms.Enums.HighSchool;
import svfc_rdms.rdms.Enums.PreSchool;
import svfc_rdms.rdms.Enums.Shs_Courses;
import svfc_rdms.rdms.ExceptionHandler.ApiRequestException;
import svfc_rdms.rdms.repository.Student.StudentRepository;
import svfc_rdms.rdms.service.Global.StudentRequest_ChartsLogicService;

@Service
public class StudentRequest_ChartsLogicServiceImpl implements StudentRequest_ChartsLogicService {

    @Autowired
    private StudentRepository studentReqRepository;

    @Override
    public ResponseEntity<?> getCountAndStatusAndYearAndCourseWhereStatusIs(String status) {
        List<Object[]> data = studentReqRepository.findCountAndRequestStatusAndYearAndCourseWhereStatusIs(status);
        List<Object[]> modifiedData = new ArrayList<>();
    
        for (Object[] obj : data) {
            Object[] newObj = new Object[obj.length+2]; // Create new array with increased size
            System.arraycopy(obj, 0, newObj, 0, obj.length); // Copy existing elements to new array
            
            // Check if obj[1] is equal to any of the enum values
            String course = obj[1].toString();
            boolean found = false;
            for (College_Courses cc : College_Courses.values()) {
                if (course.equalsIgnoreCase(cc.name())) {
                    newObj[4] = "College";
                    newObj[5] = cc.getDegree();
                    found = true;
                    break;
                }
            }
            if (!found) {
                for (Shs_Courses sc : Shs_Courses.values()) {
                    if (course.equalsIgnoreCase(sc.name())) {
                        newObj[4] = "SHS";
                        newObj[5] = sc.getValue();
                        found = true;
                        break;
                    }
                }
            }
            if (!found) {
                for (PreSchool p : PreSchool.values()) {
                    if (course.equalsIgnoreCase(p.name())) {
                        newObj[4] = "Preschool";
                        newObj[5] = p.getGrade();
                        found = true;
                        break;
                    }
                }
            }
            if (!found) {
                for (HighSchool h : HighSchool.values()) {
                    if (course.equalsIgnoreCase(h.name())) {
                        newObj[4] = "Highschool";
                        newObj[5] = h.getGrade();
                        found = true;
                        break;
                    }
                }
            }
            if (!found) {
                newObj[4] = "N/A";
            }
           
            modifiedData.add(newObj);
        }
        if (modifiedData.isEmpty()) {
            throw new ApiRequestException("No Data has been found.");
        }
        return new ResponseEntity<>(modifiedData, HttpStatus.OK);
    }
    
    

}
