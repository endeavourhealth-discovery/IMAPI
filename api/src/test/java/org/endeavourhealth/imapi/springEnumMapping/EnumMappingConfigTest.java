package org.endeavourhealth.imapi.springEnumMapping;

import org.endeavourhealth.imapi.controllers.WorkflowController;
import org.endeavourhealth.imapi.model.workflow.bugReport.TaskModule;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(WorkflowController.class)
public class EnumMappingConfigTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void whenPassingLowerCaseEnumConstant_thenConvert() throws Exception {
        JSONObject data = new JSONObject();
        data.put("module","directory");
        mockMvc.perform(post("/api/workflow/createBugReport",data))
            .andExpect(status().isOk()).andExpect(content().string(TaskModule.DIRECTORY.name()));
    }
}
