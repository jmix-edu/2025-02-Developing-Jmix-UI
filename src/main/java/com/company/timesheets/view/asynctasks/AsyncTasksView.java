package com.company.timesheets.view.asynctasks;


import com.company.timesheets.view.main.MainView;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.Notifications;
import io.jmix.flowui.asynctask.UiAsyncTasks;
import io.jmix.flowui.component.textfield.TypedTextField;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static java.lang.Thread.sleep;

@Route(value = "async-tasks-view", layout = MainView.class)
@ViewController(id = "ts_AsyncTasksView")
@ViewDescriptor(path = "async-tasks-view.xml")
public class AsyncTasksView extends StandardView {

    @Autowired
    private UiAsyncTasks uiAsyncTasks;
    @Autowired
    private Notifications notifications;
    @ViewComponent
    private TypedTextField<Object> inputField;

    @Subscribe(id = "performWithoutResultBtn", subject = "clickListener")
    public void onPerformWithoutResultBtnClick(final ClickEvent<JmixButton> event) {
        uiAsyncTasks.runnableConfigurer(this::voidMethod)
                .withResultHandler(() -> {
                    notifications.show("Method completed");
                })
                .runAsync();
    }

    private void voidMethod() {
        try {
            sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted", e);
        }
    }

    @Subscribe(id = "performChangesBtn", subject = "clickListener")
    public void onPerformChangesBtnClick(final ClickEvent<JmixButton> event) {
        String value = inputField.getValue();
        uiAsyncTasks.supplierConfigurer(() -> changeString(value))
                .withResultHandler(changedString -> {
                    notifications.show(changedString);
                })
                .withTimeout(3, TimeUnit.SECONDS)
                .withExceptionHandler(e -> {
                    if(e instanceof TimeoutException) {
                        notifications.create("Timeout exception!")
                                .withType(Notifications.Type.WARNING)
                                .show();
                    } else {
                        notifications.create("Unknown error")
                                .withType(Notifications.Type.WARNING)
                                .show();
                    }
                })
                .supplyAsync();
    }

    private String changeString(String value) {
        try {
            sleep(4000);
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted", e);
        }

        return (value + " changed").toUpperCase();
    }
}