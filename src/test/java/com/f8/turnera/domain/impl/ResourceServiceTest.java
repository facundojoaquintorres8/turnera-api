package com.f8.turnera.domain.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import com.f8.turnera.config.TokenUtil;
import com.f8.turnera.domain.dtos.ResourceDTO;
import com.f8.turnera.domain.dtos.ResponseDTO;
import com.f8.turnera.domain.entities.Resource;
import com.f8.turnera.domain.repositories.IResourceRepository;
import com.f8.turnera.domain.services.IOrganizationService;
import com.f8.turnera.domain.services.impl.ResourceService;
import com.f8.turnera.exception.NoContentException;

import java.util.Optional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ResourceServiceTest {

    @Mock
    private IResourceRepository resourceRepositoryMock;

    @Mock
    private IOrganizationService organizationServiceMock;

    @Mock
    private EntityManager entityManagerMock;

    private MockedStatic<TokenUtil> tokenUtilMock;

    @InjectMocks
    private ResourceService resourceService;

    @BeforeEach
    public void setUp() {
        tokenUtilMock = mockStatic(TokenUtil.class);
    }

    @AfterEach
    public void tearDown() {
        tokenUtilMock.close();
    }

    @Nested
    @DisplayName("findByIdTests")
    class FindByIdTests {

        @Test
        @DisplayName("Find by id OK")
        void findByIdOkTest() throws Exception {

            // mocks
            Resource resourceMock = new Resource();
            resourceMock.setId(123L);
            Optional<Resource> resourceOptionalMock = Optional.of(resourceMock);
            when(TokenUtil.getClaimByToken(anyString(), anyString())).thenReturn(456);
            when(resourceRepositoryMock.findByIdAndOrganizationId(anyLong(), anyLong()))
                    .thenReturn(resourceOptionalMock);

            // execute action
            ResponseDTO response = resourceService.findById("fake_token", 123L);

            // asserts
            assertNotNull(response);
            assertEquals(123, ((ResourceDTO) response.getData()).getId());

            verify(resourceRepositoryMock, times(1)).findByIdAndOrganizationId(anyLong(), anyLong());
        }

        @Test
        @DisplayName("Not found")
        void findByIdNotFoundTest() throws Exception {

            // mocks
            when(TokenUtil.getClaimByToken(anyString(), anyString())).thenReturn(456);
            when(resourceRepositoryMock.findByIdAndOrganizationId(anyLong(), anyLong())).thenReturn(Optional.empty());

            // execute action
            NoContentException ex = assertThrows(NoContentException.class, () -> {
                resourceService.findById("fake_token", 123L);
            });

            // asserts
            assertEquals("Recurso no encontrado - 123", ex.getMessage());

            verify(resourceRepositoryMock, times(1)).findByIdAndOrganizationId(anyLong(), anyLong());
        }

    }

}
