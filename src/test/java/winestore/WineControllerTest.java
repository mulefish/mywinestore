package winestore;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class WineControllerTest {

    @Mock
    private WineRepository wineRepository;

    @Mock
    private WineVectorRepository wineVectorRepository;

    @InjectMocks
    private WineController wineController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindClosestWinesWithSpicyDescriptor() {
        // Arrange: Set up a sample wine list with known "topnote" vectors
        Wine wine1 = new Wine();
        wine1.setId(1L);
        wine1.setType("Red");
        wine1.setVariety("Cabernet");
        wine1.setYear(2018);
        wine1.setRegion("Napa");
        wine1.setPrice(45);
        wine1.setTopnote("0.8,0.1,0.1");
        wine1.setBottomnote("rich");

        Wine wine2 = new Wine();
        wine2.setId(2L);
        wine2.setType("White");
        wine2.setVariety("Chardonnay");
        wine2.setYear(2020);
        wine2.setRegion("Sonoma");
        wine2.setPrice(30);
        wine2.setTopnote("0.3,0.7,0.0");
        wine2.setBottomnote("fruity");

        when(wineRepository.findAll()).thenReturn(Arrays.asList(wine1, wine2));

        // Act: Call the findClosestWines method with the "spicy" descriptor
        List<Wine> result = wineController.findClosestWines("spicy");
System.out.println("Hello");
for ( int i = 0 ; i  < result.size(); i++) {
    Wine w = result.get(i);
    System.out.println(" i " + i );
    System.out.println(w.toString());
}
        // Assert: Check if the result includes the expected wine
        assertNotNull(result);
        assertFalse(result.isEmpty(), "Expected at least one wine for descriptor 'spicy'");
        assertEquals(1L, result.get(0).getId(), "Expected the closest wine ID to be 1 for 'spicy'");
        assertEquals("Cabernet", result.get(0).getVariety(), "Expected 'Cabernet' as the variety for 'spicy'");
    }
}
