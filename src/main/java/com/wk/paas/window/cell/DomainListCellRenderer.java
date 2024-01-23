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
public class DomainListCellRenderer extends JCheckBox implements ListCellRenderer<DomainDesignVersionDTO> {

    private static final int VERTICAL_MARGIN = 3;
    private static final int HORIZONTAL_MARGIN = 1;

    public DomainListCellRenderer() {
        setOpaque(true);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends DomainDesignVersionDTO> list,
                                                  DomainDesignVersionDTO value,
                                                  int index,
                                                  boolean isSelected,
                                                  boolean cellHasFocus) {
        if (value != null) {
            DomainDesignDTO domainDesignDTO = Optional.ofNullable(value.getDomainDesignDTO()).orElse(new DomainDesignDTO());
            setText("<html><b>名称:</b> " + domainDesignDTO.getName() +
                    "<br/><b>版本号:</b> " + value.getCurrentVersion() +
                    "<html/>");
            setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
            setForeground(isSelected ? list.getSelectionForeground() : list.getForeground());
            setSelected(isSelected);
        }

        setBorder(BorderFactory.createEmptyBorder(VERTICAL_MARGIN, HORIZONTAL_MARGIN, VERTICAL_MARGIN, HORIZONTAL_MARGIN));

        return this;
    }
}
