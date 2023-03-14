package com.wk.paas.window.cell;

import com.intellij.ui.JBColor;
import com.wk.paas.service.dto.ApplicationDTO;
import com.wk.paas.service.dto.ApplicationVersionDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import java.awt.*;
import java.util.Objects;

@EqualsAndHashCode(callSuper = true)
@Data
public class AppVersionListCellRenderer extends BasicComboBoxRenderer {

    private ApplicationVersionDTO applicationVersionDTO;

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        if (value instanceof ApplicationVersionDTO) {
            applicationVersionDTO = (ApplicationVersionDTO) value;
            setText("<html>版本号：" + applicationVersionDTO.getCurrentVersion() +
                    "<html/>");
            setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
            setForeground(isSelected ? list.getSelectionForeground() : list.getForeground());
        } else {
            setText("<html>请选择应用版本号<html/>");
        }
        return this;
    }
}