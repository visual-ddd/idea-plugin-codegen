package com.wk.paas.window.cell;

import com.wk.paas.service.dto.BusinessSceneDTO;
import com.wk.paas.service.dto.BusinessSceneVersionDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;

@EqualsAndHashCode(callSuper = true)
@Data
public class BusinessListCellRenderer extends JCheckBox implements ListCellRenderer<BusinessSceneVersionDTO> {

    private static final int VERTICAL_MARGIN = 3;
    private static final int HORIZONTAL_MARGIN = 1;

    public BusinessListCellRenderer() {
        setOpaque(true);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends BusinessSceneVersionDTO> list,
                                                  BusinessSceneVersionDTO value,
                                                  int index,
                                                  boolean isSelected,
                                                  boolean cellHasFocus) {
        if (value != null) {
            BusinessSceneDTO businessSceneDTO = Optional.ofNullable(value.getBusinessSceneDTO()).orElse(new BusinessSceneDTO());
            setText("<html>名称：" + businessSceneDTO.getName() +
                    "<br/>版本号：" + value.getCurrentVersion() +
                    "<html/>");
            setFont(getFont().deriveFont(Font.BOLD));
            setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
            setForeground(isSelected ? list.getSelectionForeground() : list.getForeground());
            setSelected(isSelected);
        }

        setBorder(BorderFactory.createEmptyBorder(VERTICAL_MARGIN, HORIZONTAL_MARGIN, VERTICAL_MARGIN, HORIZONTAL_MARGIN));

        return this;
    }
}