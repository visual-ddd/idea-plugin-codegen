package com.wk.paas.window.cell;

import com.wk.paas.service.dto.TeamDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import java.awt.*;

@EqualsAndHashCode(callSuper = true)
@Data
public class TeamListCellRenderer extends BasicComboBoxRenderer {

    private TeamDTO teamDTO;

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        if (value instanceof TeamDTO) {
            teamDTO = (TeamDTO) value;
            setText("<html>团队名称：" + teamDTO.getName() +
                    "<html/>");
            setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
            setForeground(isSelected ? list.getSelectionForeground() : list.getForeground());
        } else {
            setText("<html>请选择团队<html/>");
        }
        return this;
    }
}