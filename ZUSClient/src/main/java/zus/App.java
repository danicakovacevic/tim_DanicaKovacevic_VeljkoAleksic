package zus;

import javafx.application.Application;
import javafx.stage.Stage;
import zus.model.utility.JDBCUtils;
import zus.view.LoginView;

public class App extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        JDBCUtils.connect();
        stage = new LoginView();
        stage.show();
    }
}
