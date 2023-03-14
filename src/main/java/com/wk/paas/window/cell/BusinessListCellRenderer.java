package com.wk.paas.window.cell;

import com.wk.paas.service.dto.BusinessSceneDTO;
import com.wk.paas.service.dto.BusinessSceneVersionDTO;
import com.wk.paas.service.dto.DomainDesignDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;

@EqualsAndHashCode(callSuper = true)
@Data
public class BusinessListCellRenderer extends JCheckBox implements ListCellRenderer {

    private BusinessSceneVersionDTO businessSceneVersionDTO;

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        if (value instanceof BusinessSceneVersionDTO) {
            businessSceneVersionDTO = (BusinessSceneVersionDTO) value;
            BusinessSceneDTO businessSceneDTO = Optional.ofNullable(businessSceneVersionDTO.getBusinessSceneDTO()).orElse(new BusinessSceneDTO());
            setText("<html>名称：" + businessSceneDTO.getName() +
//                    "<br/>描述：" + businessSceneDTO.getDescription() +
                    "<br/>版本号：" + businessSceneVersionDTO.getCurrentVersion() +
                    "<html/>");
            setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
            setForeground(isSelected ? list.getSelectionForeground() : list.getForeground());
            setSelected(isSelected);
        }
        return this;
    }
}