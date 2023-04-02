package svfc_rdms.rdms.service.Global;

import org.springframework.http.ResponseEntity;

public interface StudentRequest_ChartsLogicService {

    ResponseEntity<?> getCountAndStatusAndYearAndCourseWhereStatusIs(String status);
}
