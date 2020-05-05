package logic.neptun_data;

import java.util.List;

public class NeptunDataHandlerThread extends Thread {
    private List<String> subjectNames;
    private String neptunCode;
    private String neptunPassword;

    public NeptunDataHandlerThread(String neptunCode, String neptunPassword) {
        this.neptunCode = neptunCode;
        this.neptunPassword = neptunPassword;
        this.subjectNames = null;
    }

    @Override
    public void run() {
        this.subjectNames = NeptunDataHandler.getNeptunDataHandler()
                .getSubjectNames(neptunCode, neptunPassword);
    }

    public List<String> getSubjectNames() {
        return subjectNames;
    }
}
