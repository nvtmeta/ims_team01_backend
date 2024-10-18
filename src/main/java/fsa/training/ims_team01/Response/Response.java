package fsa.training.ims_team01.Response;

public class Response {
    private boolean success;
    private String message;
    private Object data;

    // getters and setters

    public Response() {}

    public Response(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public Response(boolean success, String message, Object data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }
}