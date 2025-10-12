package ana.tret.amlcheckservice.exception;

public class RecordNotFoundException extends RuntimeException {

    public RecordNotFoundException(String msg) {
        super(msg);
    }

    public static RecordNotFoundException notFoundById(Long id) {
        return new RecordNotFoundException("record not found by id %s".formatted(id));
    }
}
