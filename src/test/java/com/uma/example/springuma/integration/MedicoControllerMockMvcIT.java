package com.uma.example.springuma.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfEnvironmentVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uma.example.springuma.integration.base.AbstractIntegration;
import com.uma.example.springuma.model.Medico;
import com.uma.example.springuma.model.Paciente;

public class MedicoControllerMockMvcIT extends AbstractIntegration {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Medico medico;

    @BeforeEach
    void setUp() {
        medico = new Medico();
        medico.setId(1L);
        medico.setDni("835");
        medico.setNombre("Miguel");
        medico.setEspecialidad("Ginecologia");
    }

    private void crearMedico(Medico medico) throws Exception {
        this.mockMvc.perform(post("/medico")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(medico)))
                .andExpect(status().isCreated());
    }

    private void getMedicoById(Long id, Medico expected) throws Exception {
        mockMvc.perform(get("/medico/" + id))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expected)));
    }

    @Test
    @DisplayName("Crear un medico con POST y verificar que se ha creado correctamente")
    void createMedicoPost_isCreated() throws Exception {
        crearMedico(medico);
    }
    
    @Test
    @DisplayName("Actualizar un medico con PUT y verificar que se ha actualizado correctamente")
    void updateMedicoPut_isNoContent_and_MedicoIsUpdatedObtainedWithGet() throws Exception {
        crearMedico(medico);
        medico.setEspecialidad("Pediatria");
        this.mockMvc.perform(put("/medico")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(medico)))
                .andExpect(status().isNoContent());
        
        getMedicoById(medico.getId(), medico);
    }

    @Test
    @DisplayName("Crear un medico con POST y verificar que se ha creado correctamente con GET")
    void createMedicoPost_isObtainedWithGet() throws Exception {
        crearMedico(medico);
        getMedicoById(medico.getId(), medico);
    }

    @Test
    @DisplayName("Crear un medico con POST y verificar que se ha creado correctamente con GET por DNI")
    void createMedicoPost_isObtainedByDNIWithGet() throws Exception {
        crearMedico(medico);
        getMedicoById(medico.getId(), medico);
    }

    @Test
    @DisplayName("Crear un medico por POST y verificar que se ha borrar correctamente con DELETE")
    void deleteMedico_isOk() throws Exception {
        crearMedico(medico);
        this.mockMvc.perform(delete("/medico/" + medico.getId()))
                .andExpect(status().isOk());
    }
}
