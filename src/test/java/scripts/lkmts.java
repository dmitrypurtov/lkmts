package scripts;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import containers.User;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import services.FileService;
import settings.SelenideTool;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;


public class lkmts {
    private static final Integer SLEEP_THREAD_COUNT = 30; // 10c*10=100c=1.40мин
    private static final Integer SLEEP_THREAD_TIME = 30000; //10с
    private List<User> users;
    private List<String> servicesOff;
    private List<String> servicesOn;
    private Integer waitMinutes;

    @BeforeTest
    @Parameters({"userFilePath", "serviceOff", "serviceOn", "waitMinutesNewService"})
    public void init(String userFilePath, String serviceOff, String serviceOn, Integer waitMinutesNewService) throws IOException {
        new SelenideTool();
        Type listType = new TypeToken<List<String>>() {
        }.getType();
        servicesOff = new Gson().fromJson(serviceOff, listType);
        servicesOn = new Gson().fromJson(serviceOn, listType);
        users = FileService.readUseAccesses(userFilePath);
        waitMinutes = waitMinutesNewService * 1000 * 60;
    }

    @Test
    public void serviceUpdate() {
        for (User user : users) {
            FileService.writeToDetailLog("\n\n");
            FileService.writeToDetailLog("Проверка что нет авторизованного пользователя");
            /*Вход в личный кабинет*/
            LkMtsPage lkMts = new LkMtsPage();
            if (lkMts.isAuthorized()) {
                lkMts.singOut(waitMinutes);
            }

            FileService.writeToDetailLog("Вход на страницу с логинацией");
            lkMts.openPage().signIn(user.login, user.password).changePassword(user.password);
            SelenideTool.waitTime(SLEEP_THREAD_TIME);
            SelenideTool.forcedRefresh();
            if (!lkMts.isAuthorized()) {
                continue;
            }
            FileService.writeToDetailLog("Вход пользователя: " + user.login);
            lkMts.offSmsInSetting();

            lkMts.swichToServices();
            FileService.writeToDetailLog("Переход на страницу услуг");
            FileService.writeToDetailLog("\n");
            SelenideTool.waitTime(SLEEP_THREAD_TIME);
            /*Выключение услуг*/
            if (lkMts.isSwichToConnectedServices()) {
                lkMts.swichToConnectedServices();
                FileService.writeToDetailLog("Отображение всех подключенных услуг (всплывающие списки)");
                for (String service : servicesOff) {
                    FileService.writeToDetailLog("Проверка выключенны ли уже услуга \"" + service + "\"");
                    if (!lkMts.isOffServiceInPage(service)) {
                        FileService.writeToDetailLog("Пропуск отключения услуги \"" + service + "\" из-за того что она уже отключенна");
                        continue;
                    }
                    FileService.writeToDetailLog("Выключение услуги \"" + service + "\"");
                    lkMts.offService(service);
                    for (int i = 0; i < SLEEP_THREAD_COUNT; i++) {
                        SelenideTool.waitTime(SLEEP_THREAD_TIME);
                        SelenideTool.forcedRefresh();
                        SelenideTool.waitTime(SLEEP_THREAD_TIME);
                        lkMts.swichToConnectedServices();
                        if (!lkMts.isOffServiceInPage(service)) {
                            break;
                        }
                    }
                    FileService.writeToDetailLog("Проверка что услуга \"" + service + "\" выключенна");
                    if (lkMts.isOffServiceInPage(service)) {
                        Assert.assertTrue(false, user.login + "\t" + user.password + "\tОшибка при ВЫКЛЮЧЕНИИ услуги \"" + service + "\"");
                    }
                }
            }
            FileService.writeToDetailLog("\n");

            /*Включение услуг*/
            if (lkMts.isSwichToAvailableServices()) {
                lkMts.swichToAvailableServices();
                FileService.writeToDetailLog("Отображение всех подключаемых услуг услуг (всплывающие списки)");
                for (String service : servicesOn) {
                    FileService.writeToDetailLog("Проверка подключенна ли уже услуга \"" + service + "\"");
                    if (!lkMts.isOnService(service)) {
                        FileService.writeToDetailLog("Пропуск подключенной услуги \"" + service + "\" из-за того что она уже подключенна");
                        continue;
                    }
                    FileService.writeToDetailLog("Подключение услуги \"" + service + "\"");
                    lkMts.onService(service);
                    for (int i = 0; i < SLEEP_THREAD_COUNT; i++) {
                        SelenideTool.waitTime(SLEEP_THREAD_TIME);
                        SelenideTool.forcedRefresh();
                        lkMts.swichToAvailableServices();
                        if (!lkMts.isOnService(service)) {
                            break;
                        }
                    }
                    FileService.writeToDetailLog("Проверка что услуга \"" + service + "\" подключенна");
                    if (lkMts.isOnService(service)) {
                        Assert.assertTrue(false, user.login + "\t" + user.password + "\tОшибка при ВКЛЮЧЕНИИ услуги \"" + service + "\"");
                    }
                }
            }

            FileService.writeToDetailLog("Выход из системы");
            lkMts.closePopup();
            lkMts.singOut(waitMinutes);
        }
    }
}