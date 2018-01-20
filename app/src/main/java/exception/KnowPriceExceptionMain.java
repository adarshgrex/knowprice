package exception;

/**
 * Created by adarsh on 7/12/16.
 */
public class KnowPriceExceptionMain extends RuntimeException{

    private static final long serialVersionUID = 1L;
    //    private static final long serialVersionUID = 7119540893582232697L;
    String customMessage;

    public KnowPriceExceptionMain(String customMessage) {
        super();
        this.customMessage = customMessage;
    }

    public KnowPriceExceptionMain() {
    }

    public String getCustomMessage() {
        return customMessage;
    }
}
