/*
 * Copyright (C) 2011 4th Line GmbH, Switzerland
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 2 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.fourthline.cling.workbench.plugins.igd;

import org.fourthline.cling.model.ModelUtil;
import org.fourthline.cling.support.model.Connection;
import org.seamless.swing.Form;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * @author Christian Bauer
 */
public class WANIPConnectionViewImpl extends JDialog implements WANIPConnectionView {

    public static class ConnectionInfoPanel extends JPanel {

        final protected JTextField statusField = new JTextField();
        final protected JTextField uptimeField = new JTextField();
        final protected JTextField lastErrorField = new JTextField();
        final protected JTextField ipField = new JTextField();

        public ConnectionInfoPanel() {
            super(new GridBagLayout());

            statusField.setEditable(false);
            uptimeField.setEditable(false);
            lastErrorField.setEditable(false);
            ipField.setEditable(false);

            Form form = new Form(3);
            setLabelAndTextField(form, "Connection Status:", statusField);
            setLabelAndTextField(form, "Connection Uptime:", uptimeField);
            setLabelAndTextField(form, "Last Error:", lastErrorField);
            setLabelAndTextField(form, "External IP Address:", ipField);
        }

        public void updateIP(String ip) {
            ipField.setText(ip);
        }

        public void updateStatus(Connection.StatusInfo statusInfo) {
            statusField.setText(statusInfo.getStatus().name());
            uptimeField.setText(ModelUtil.toTimeString(statusInfo.getUptimeSeconds()));
            lastErrorField.setText(statusInfo.getLastError().name());
        }

        protected void setLabelAndTextField(Form form, String l, Component field) {
            JLabel label = new JLabel(l);
            label.setBorder(BorderFactory.createEmptyBorder(6, 0, 0, 0));
            form.addLabel(label, this);
            form.addLastField(field, this);
        }

    }

    final protected ConnectionInfoPanel connectionInfoPanel = new ConnectionInfoPanel();

    @Inject
    protected PortMappingListView portMappingListView;

    @Inject
    protected PortMappingEditView portMappingEditView;

    protected Presenter presenter;

    @PostConstruct
    public void init() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                dispose();
            }
        });

        connectionInfoPanel.setBorder(new EmptyBorder(0, 0, 5, 0));

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.add(connectionInfoPanel, BorderLayout.NORTH);
        mainPanel.add(portMappingListView.asUIComponent(), BorderLayout.CENTER);
        mainPanel.add(portMappingEditView.asUIComponent(), BorderLayout.SOUTH);

        add(mainPanel);
        setMinimumSize(new Dimension(600, 550));
        setPreferredSize(new Dimension(600, 550));
        pack();
        setVisible(true);
    }

    @Override
    public Component asUIComponent() {
        return this;
    }

    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public PortMappingListView getPortMappingListView() {
        return portMappingListView;
    }

    @Override
    public PortMappingEditView getPortMappingEditView() {
        return portMappingEditView;
    }

    @Override
    public void setExternalIP(String ip) {
        connectionInfoPanel.updateIP(ip);
    }

    @Override
    public void setStatusInfo(Connection.StatusInfo statusInfo) {
        connectionInfoPanel.updateStatus(statusInfo);
    }
}
