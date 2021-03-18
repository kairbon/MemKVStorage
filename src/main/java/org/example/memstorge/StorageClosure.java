package org.example.memstorge;

/**
 * @author : kairbon
 * @date : 2021/3/18
 */
public class StorageClosure {

  private String errorMsg;

  private Object data;

  private boolean success;

  public boolean isSuccess() {
    return success;
  }

  public void setSuccess(boolean success) {
    this.success = success;
  }

  public String getErrorMsg() {
    return errorMsg;
  }

  void setErrorMsg(String errorMsg) {
    this.errorMsg = errorMsg;
  }

  public Object getData() {
    return data;
  }

  void setData(Object data) {
    this.data = data;
  }

}
