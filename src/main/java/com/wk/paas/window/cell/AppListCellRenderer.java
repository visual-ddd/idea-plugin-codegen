package com.wk.paas.window.cell;

import com.wk.paas.service.dto.ApplicationDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import java.awt.*;

@EqualsAndHashCode(callSuper = true)
@Data
public class AppListCellRenderer extends BasicComboBoxRenderer {

    private ApplicationDTO applicationDTO;

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        if (value instanceof ApplicationDTO) {
            applicationDTO = (ApplicationDTO) value;
            setText("<html>应用名称：" + applicationDTO.getName() +
                    "<html/>");
            setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
            setForeground(isSelected ? list.getSelectionForeground() : list.getForeground());
        } else {
            setText("<html>请选择应用<html/>");
        }
        return this;
    }
}