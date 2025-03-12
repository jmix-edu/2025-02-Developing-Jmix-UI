package com.company.timesheets.action;

import com.company.timesheets.app.TimeEntrySupport;
import com.company.timesheets.entity.TimeEntry;
import com.company.timesheets.view.timeentry.TimeEntryDetailView;
import com.vaadin.flow.component.icon.VaadinIcon;
import io.jmix.core.Messages;
import io.jmix.core.metamodel.model.MetaClass;
import io.jmix.core.security.EntityOp;
import io.jmix.flowui.DialogWindows;
import io.jmix.flowui.accesscontext.UiEntityContext;
import io.jmix.flowui.action.ActionType;
import io.jmix.flowui.action.AdjustWhenViewReadOnly;
import io.jmix.flowui.action.list.SecuredListDataComponentAction;
import io.jmix.flowui.data.EntityDataUnit;
import io.jmix.flowui.kit.component.ComponentUtils;
import io.jmix.flowui.view.DialogWindow;
import io.jmix.flowui.view.StandardOutcome;
import io.jmix.flowui.view.builder.DetailWindowBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;

import java.util.function.Consumer;
import java.util.function.Function;

@ActionType(TimeEntryCopyAction.ID)
public class TimeEntryCopyAction
        // Action that changes its enabled property depending on a selected item in a bound Grid.
        extends SecuredListDataComponentAction<TimeEntryCopyAction, TimeEntry>
        // Interface to be implemented by actions which may adjust their 'enabled' state according to the view read-only mode.
        implements AdjustWhenViewReadOnly {

    public final static String ID = "list_timeEntry_copy";

    private DialogWindows dialogWindows;
    private TimeEntrySupport timeEntrySupport;

    private Consumer<DialogWindow.AfterCloseEvent<TimeEntryDetailView>> afterCloseHandler;
    private Consumer<TimeEntry> afterSaveHandler;
    private Function<TimeEntry, TimeEntry> transformation;

    private boolean ownTimeEntry = false;

    public TimeEntryCopyAction() {
        this(ID);
    }

    public TimeEntryCopyAction(String id) {
        super(id);
    }

    @Override
    protected void initAction() {
        super.initAction();

        setConstraintEntityOp(EntityOp.CREATE);
        this.icon = ComponentUtils.convertToIcon(VaadinIcon.COPY_O);
    }

    @Autowired
    public void setMessages(Messages messages) {
        this.text = messages.getMessage("actions.Copy");
    }

    @Autowired
    public void setDialogWindowBuilders(DialogWindows dialogWindows) {
        this.dialogWindows = dialogWindows;
    }

    @Autowired
    public void setTimeEntrySupport(TimeEntrySupport timeEntrySupport) {
        this.timeEntrySupport = timeEntrySupport;
    }

    public void setAfterCloseHandler(
            @Nullable Consumer<DialogWindow.AfterCloseEvent<TimeEntryDetailView>> afterCloseHandler) {
        this.afterCloseHandler = afterCloseHandler;
    }

    public void setAfterSaveHandler(@Nullable Consumer<TimeEntry> afterSaveHandler) {
        this.afterSaveHandler = afterSaveHandler;
    }

    public void setTransformation(@Nullable Function<TimeEntry, TimeEntry> transformation) {
        this.transformation = transformation;
    }

    public boolean isOwnTimeEntry() {
        return ownTimeEntry;
    }

    public void setOwnTimeEntry(boolean ownTimeEntry) {
        this.ownTimeEntry = ownTimeEntry;
    }

    @Override
    protected boolean isPermitted() {
        if (target == null
                || target.getSingleSelectedItem() == null
                || !(target.getItems() instanceof EntityDataUnit)) {
            return false;
        }

        MetaClass metaClass = ((EntityDataUnit) target.getItems()).getEntityMetaClass();

        UiEntityContext entityContext = new UiEntityContext(metaClass);
        accessManager.applyRegisteredConstraints(entityContext);

        if (!entityContext.isCreatePermitted()) {
            return false;
        }

        return super.isPermitted();
    }

    @Override
    public void execute() {
        checkTarget();
        checkTargetItems(EntityDataUnit.class);

        TimeEntry selectedItem = target.getSingleSelectedItem();
        if (selectedItem == null) {
            throw new IllegalStateException(String.format("There is not selected item in %s target",
                    getClass().getSimpleName()));
        }

        TimeEntry copiedEntity = timeEntrySupport.copy(selectedItem);
        openDialog(copiedEntity);
    }

    private void openDialog(TimeEntry timeEntry) {
        DetailWindowBuilder<TimeEntry, TimeEntryDetailView> builder =
                dialogWindows.detail(target)
                        .withViewClass(TimeEntryDetailView.class)
                        .newEntity(timeEntry);

        if (afterCloseHandler != null) {
            builder = builder.withAfterCloseListener(afterCloseHandler);
        }

        if (transformation != null) {
            builder = builder.withTransformation(transformation);
        }

        DialogWindow<TimeEntryDetailView> dialogWindow = builder.build();
        TimeEntryDetailView detailView = dialogWindow.getView();
        detailView.setOwnTimeEntry(ownTimeEntry);
        if (afterSaveHandler != null) {
            dialogWindow.addAfterCloseListener(event -> {
                if (event.closedWith(StandardOutcome.SAVE)) {
                    TimeEntry savedEntity = detailView.getEditedEntity();
                    afterSaveHandler.accept(savedEntity);
                }
            });
        }

        dialogWindow.open();

    }

    @Override
    public boolean isDisabledWhenViewReadOnly() {
        if (!(target.getItems() instanceof EntityDataUnit)) {
            return true;
        }

        MetaClass metaClass = ((EntityDataUnit) target.getItems()).getEntityMetaClass();
        // Even though the view is read-only, this edit action may remain active
        // because the related entity cannot be edited and the corresponding detail view
        // will be opened in read-only mode either.
        UiEntityContext entityContext = new UiEntityContext(metaClass);
        accessManager.applyRegisteredConstraints(entityContext);

        return entityContext.isCreatePermitted();
    }

    /**
     * @see #setAfterCloseHandler(Consumer)
     */
    public TimeEntryCopyAction withAfterCloseHandler(
            Consumer<DialogWindow.AfterCloseEvent<TimeEntryDetailView>> afterCloseHandler) {
        setAfterCloseHandler(afterCloseHandler);
        return this;
    }

    /**
     * @see #setAfterSaveHandler(Consumer)
     */
    public TimeEntryCopyAction withAfterSaveHandler(@Nullable Consumer<TimeEntry> afterSaveHandler) {
        setAfterSaveHandler(afterSaveHandler);
        return this;
    }

    /**
     * @see #setTransformation(Function)
     */
    public TimeEntryCopyAction withTransformation(@Nullable Function<TimeEntry, TimeEntry> transformation) {
        setTransformation(transformation);
        return this;
    }

    /**
     * @see #setOwnTimeEntry(boolean)
     */
    public TimeEntryCopyAction withOwnTimeEntry(boolean ownTimeEntry) {
        setOwnTimeEntry(ownTimeEntry);
        return this;
    }

}
