package svfc_rdms.rdms.service.Document;

import java.util.Optional;

import svfc_rdms.rdms.model.Documents;

public interface DocumentService {

     Optional<Documents> getFileDocumentById(long id);
}
