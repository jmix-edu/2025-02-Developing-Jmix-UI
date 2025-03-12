package com.company.timesheets.component;

import com.vaadin.flow.component.shared.Tooltip;
import io.jmix.flowui.kit.meta.*;

@StudioUiKit
public interface StudioComponents {
    @StudioComponent( name = "ThemeToggle",
            classFqn = "com.company.timesheets.component.themetoggle.ThemeToggle",
            category = "Components",
            xmlElement = "themeToggle",
            xmlns = "http://company.com/schema/app-ui-components",
            xmlnsAlias = "app",
            properties = {
                    /* Common attributes */
                    @StudioProperty(xmlAttribute = "id", type = StudioPropertyType.COMPONENT_ID),
                    @StudioProperty(xmlAttribute = "alignSelf", type = StudioPropertyType.ENUMERATION,
                            classFqn = "com.vaadin.flow.component.orderedlayout.FlexComponent$Alignment",
                            defaultValue = "AUTO",
                            options = {"START", "END", "CENTER", "STRETCH", "BASELINE", "AUTO"}),
                    @StudioProperty(xmlAttribute = "autofocus", type = StudioPropertyType.BOOLEAN),

                    @StudioProperty(xmlAttribute = "storageKey", type = StudioPropertyType.STRING),
                    @StudioProperty(xmlAttribute = "themeNames", type = StudioPropertyType.STRING),

                    @StudioProperty(xmlAttribute = "text", type = StudioPropertyType.STRING),
                    @StudioProperty(xmlAttribute = "enabled", type = StudioPropertyType.BOOLEAN, defaultValue = "true"),
                    @StudioProperty(xmlAttribute = "classNames", type = StudioPropertyType.STRING),


                    /* Size attributes */
                    @StudioProperty(xmlAttribute = "height", type = StudioPropertyType.SIZE),
                    @StudioProperty(xmlAttribute = "width", type = StudioPropertyType.SIZE),
                    @StudioProperty(xmlAttribute = "maxHeight", type = StudioPropertyType.SIZE),
                    @StudioProperty(xmlAttribute = "maxWidth", type = StudioPropertyType.SIZE),
                    @StudioProperty(xmlAttribute = "minHeight", type = StudioPropertyType.SIZE),
                    @StudioProperty(xmlAttribute = "minWidth", type = StudioPropertyType.SIZE),
            })
    void themeToggle();

    @StudioElement(
            name = "Tooltip",
            classFqn = "com.vaadin.flow.component.shared.Tooltip",
            icon = "io/jmix/flowui/kit/meta/icon/element/tooltip.svg",
            xmlElement = "tooltip",
            xmlns = "http://company.com/schema/app-ui-components",
            xmlnsAlias = "app",
            documentationLink = "%VERSION%/flow-ui/vc/components/tooltip.html",
            unlimitedCount = false,
            target = {"com.company.timesheets.component.theme_toggle.ThemeToggle"},
            properties = {
                    @StudioProperty(xmlAttribute = "text", type = StudioPropertyType.LOCALIZED_STRING, required = true),
                    @StudioProperty(xmlAttribute = "focusDelay", type = StudioPropertyType.INTEGER),
                    @StudioProperty(xmlAttribute = "hideDelay", type = StudioPropertyType.INTEGER),
                    @StudioProperty(xmlAttribute = "hoverDelay", type = StudioPropertyType.INTEGER),
                    @StudioProperty(xmlAttribute = "manual", type = StudioPropertyType.BOOLEAN),
                    @StudioProperty(xmlAttribute = "opened", type = StudioPropertyType.BOOLEAN),
                    @StudioProperty(xmlAttribute = "position", type = StudioPropertyType.ENUMERATION,
                            classFqn = "com.vaadin.flow.component.shared.Tooltip$TooltipPosition",
                            setParameterFqn = "com.vaadin.flow.component.shared.Tooltip$TooltipPosition",
                            options = {"TOP_START", "TOP", "TOP_END", "BOTTOM_START", "BOTTOM", "BOTTOM_END",
                                    "START_TOP", "START", "START_BOTTOM", "END_TOP", "END", "END_BOTTOM"})
            }
    )
    Tooltip tooltip();
}
