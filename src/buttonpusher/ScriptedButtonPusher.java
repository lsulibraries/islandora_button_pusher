/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package buttonpusher;

import java.util.List;

/**
 *
 * @author jpeak5
 */
public class ScriptedButtonPusher extends ButtonPusher {
    public ScriptedButtonPusher() {
        super();
        this.host = "lib-dig003.lsu.edu:8000";
    }

    @Override
    void visitItems(List<String> items) {
        for (String url : items) {
            driver.get("http://" + this.host + "/" + url);
        }
    }
}
