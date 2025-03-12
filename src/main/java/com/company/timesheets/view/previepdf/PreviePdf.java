package com.company.timesheets.view.previepdf;


import com.company.timesheets.view.main.MainView;
import com.vaadin.componentfactory.pdfviewer.PdfViewer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import io.jmix.core.Resources;
import io.jmix.flowui.view.StandardView;
import io.jmix.flowui.view.Subscribe;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "previe-pdf", layout = MainView.class)
@ViewController(id = "ts_PreviePdf")
@ViewDescriptor(path = "previe-pdf.xml")
public class PreviePdf extends StandardView {
    @Autowired
    private Resources resources;

    @Subscribe
    public void onInit(final InitEvent event) {

        PdfViewer pdfViewer = new PdfViewer();
        pdfViewer.setSizeFull();
        StreamResource resource = new StreamResource("example.pdf", () ->
                resources.getResourceAsStream("META-INF/resources/files/example.pdf"));
        pdfViewer.setSrc(resource);

        getContent().add(pdfViewer);
    }
}