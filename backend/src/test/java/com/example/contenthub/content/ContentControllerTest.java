package com.example.contenthub.content;

import com.example.contenthub.content.enums.ContentType;

import com.example.contenthub.content.dto.request.ContentSearchRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class ContentControllerTest {
    private final ContentService service = mock(ContentService.class);
    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(new ContentController(service)).build();
        when(service.filterContents(any())).thenReturn(List.of());
    }

    @Test
    void canonicalRequestNormalizesTypeAndFilters() throws Exception {
        mvc.perform(get("/contents").param("type", " novel ")
                .param("genre", " fantasy ").param("title", " title "))
                .andExpect(status().isOk()).andExpect(content().json("[]"));
        ArgumentCaptor<ContentSearchRequest> request = ArgumentCaptor.forClass(ContentSearchRequest.class);
        verify(service).filterContents(request.capture());
        assertEquals(ContentType.NOVEL, request.getValue().getType());
        assertEquals("fantasy", request.getValue().getGenre());
        assertEquals("title", request.getValue().getTitle());
    }

    @Test
    void oldSearchPathAndCategoryRemainSupported() throws Exception {
        mvc.perform(get("/contents/search").param("category", "webtoon"))
                .andExpect(status().isOk());
        ArgumentCaptor<ContentSearchRequest> request = ArgumentCaptor.forClass(ContentSearchRequest.class);
        verify(service).filterContents(request.capture());
        assertEquals(ContentType.WEBTOON, request.getValue().getType());
    }

    @Test
    void emptyFiltersMeanNoFilter() throws Exception {
        mvc.perform(get("/contents").param("type", "").param("genre", " ").param("title", ""))
                .andExpect(status().isOk());
        ArgumentCaptor<ContentSearchRequest> request = ArgumentCaptor.forClass(ContentSearchRequest.class);
        verify(service).filterContents(request.capture());
        assertNull(request.getValue().getType());
        assertNull(request.getValue().getGenre());
        assertNull(request.getValue().getTitle());
    }

    @Test
    void unsupportedTypeAndConflictingAliasesReturnBadRequest() throws Exception {
        mvc.perform(get("/contents").param("type", "animation")).andExpect(status().isBadRequest());
        mvc.perform(get("/contents").param("type", "NOVEL").param("category", "MOVIE"))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(service);
    }
}
