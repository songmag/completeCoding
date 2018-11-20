package client;

public interface ILoginCallback {
    void StartService();
    void FailToLogin();
    void NoData();
}
