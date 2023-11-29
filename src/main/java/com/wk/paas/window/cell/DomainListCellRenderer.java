package com.wk.paas.window.cell;

import com.wk.paas.service.dto.DomainDesignDTO;
import com.wk.paas.service.dto.DomainDesignVersionDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;

@EqualsAndHashCode(callSuper = true)
@Data
public class DomainListCellRenderer extends JCheckBox implements ListCellRenderer {

    private DomainDesignVersionDTO domainDesignVersionDTO;

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        if (value instanceof DomainDesignVersionDTO) {
            domainDesignVersionDTO = (DomainDesignVersionDTO) value;
            DomainDesignDTO domainDesignDTO = Optional.ofNullable(domainDesignVersionDTO.getDomainDesignDTO()).orElse(new DomainDesignDTO());
            setText("<html>名称：" + domainDesignDTO.getName() +
//                    "<br/>描述：" + domainDesignVersionDTO.getDescription() +
                    "<br/>版本号：" + domainDesignVersionDTO.getCurrentVersion() +
                    "<html/>");
            setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
            setForeground(isSelected ? list.getSelectionForeground() : list.getForeground());
            setSelected(isSelected);
        }
        return this;
    }

}