package org.example.rekollectapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.rekollectapi.dto.request.CreatorRequestDTO;
import org.example.rekollectapi.dto.request.RecordRequestDTO;
import org.example.rekollectapi.dto.response.CreatorWithRoleResponseDTO;
import org.example.rekollectapi.dto.response.RecordResponseDTO;
import org.example.rekollectapi.dto.response.TagResponseDTO;
import org.example.rekollectapi.service.impl.RecordServiceImpl;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.util.List;
import java.util.UUID;

import static org.mockito.BDDMockito.given;


@WebMvcTest(controllers = RecordController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class RecordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RecordServiceImpl recordService;

    @Autowired
    private ObjectMapper objectMapper;

    private RecordRequestDTO recordRequestDTO;
    private RecordResponseDTO recordResponseDTO;
    private CreatorWithRoleResponseDTO creatorResponseDTO;
    private CreatorRequestDTO creatorRequestDTO;
    private TagResponseDTO tagResponseDTO;

    @BeforeEach
    void init() {
        creatorRequestDTO = CreatorRequestDTO.builder()
                .creatorFirstName("First Name")
                .creatorLastName("Last Name")
                .creatorRole("Role Name")
                .build();

        recordRequestDTO = RecordRequestDTO.builder()
                .title("Title")
                .description("Description")
                .categoryName("Category Name")
                .creators(List.of(creatorRequestDTO))
                .tags(List.of("Tag1", "Tag2"))
                .build();

        creatorResponseDTO = CreatorWithRoleResponseDTO.builder()
                .id(UUID.fromString("a4085153-a1ee-4fec-812a-549939305b6a"))
                .creatorFirstName("First Name")
                .creatorLastName("Last Name")
                .creatorRole("Role Name")
                .build();


        tagResponseDTO = TagResponseDTO.builder()
                .id(1L)
                .tagName("Tag1")
                .build();

        recordResponseDTO = RecordResponseDTO.builder()
                .id(UUID.fromString("a4085153-a1ee-4fec-812a-549939305b6a"))
                .title("Title")
                .description("Description")
                .category("Category Name")
                .creators(List.of(creatorResponseDTO))
                .tags(List.of(tagResponseDTO))
                .build();
    }

    @Test
    public void RecordController_CreateRecord_ReturnCreated() throws Exception {
        given(recordService.createRecord(ArgumentMatchers.any(RecordRequestDTO.class), ArgumentMatchers.any(UUID.class)))
                .willReturn(recordResponseDTO);


        ResultActions response = mockMvc.perform(post("/api/records")
                .header("X-User-Id", UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(recordRequestDTO)));


        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", CoreMatchers.is(recordResponseDTO.getTitle())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", CoreMatchers.is(recordResponseDTO.getDescription())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.category", CoreMatchers.is(recordResponseDTO.getCategory())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.creators").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.creators[0].creatorFirstName", CoreMatchers.is("First Name")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.tags").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.tags[0].tagName", CoreMatchers.is("Tag1")));
    }

}