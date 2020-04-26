package edu.brown.cs.term_project.handlers;

/**
 * A class for a standard JSON response for the handlers.
 */
public class StandardResponse {
  private int status;
  private String message;

  /**
   * Constructor for the response.
   * @param status 0 successful, 1 error
   * @param message error message if error
   */
  public StandardResponse(int status, String message) {
    this.status = status;
    this.message = message;
  }

  /**
   * Sets status of response
   * @param status new status
   */
  public void setStatus(int status) {
    this.status = status;
  }

  /**
   * Sets message.
   * @param message new message
   */
  public void setMessage(String message) {
    this.message = message;
  }

  /**
   * Sets an error message and status to error.
   * @param message the error message.
   */
  public void setErrorMessage(String message) {
    this.status = 1;
    this.message = message;
  }
}
