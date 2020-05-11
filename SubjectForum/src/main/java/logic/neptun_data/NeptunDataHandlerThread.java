package logic.neptun_data;

import java.util.List;
import java.util.Map;

public class NeptunDataHandlerThread extends Thread {
    private Map<String, String> subjectNames;
    private final String neptunCode;
    private final String neptunPassword;
    private boolean finish;

    public NeptunDataHandlerThread(String neptunCode, String neptunPassword, boolean finish) {
        this.neptunCode = neptunCode;
        this.neptunPassword = neptunPassword;
        this.subjectNames = null;
        this.finish = finish;
    }

    @Override
    public void run() {
        this.subjectNames = NeptunDataHandler.getNeptunDataHandler()
                .getSubjectNames(neptunCode, neptunPassword);
        this.finish = true;
    }

    public Map<String, String> getSubjectNames() {
        return subjectNames;
    }

    public boolean isFinish() {
        return finish;
    }
}
