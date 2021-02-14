package me.summerbell.whiteshipspringbootrestapi.events;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class EventTest {

    @Test
    void builder(){
        Event event = Event.builder().build();
        assertThat(event).isNotNull();
    }

    @Test
    void javaBean(){
        String eventName = "Event";
        String eventDescription = "Spring";

        Event event = new Event();
        event.setName(eventName);
        event.setDescription(eventDescription);

        assertThat(event.getName()).isEqualTo(eventName);
        assertThat(event.getDescription()).isEqualTo(eventDescription);
    }

    @ParameterizedTest
    @MethodSource("paramsForTestFree")
    void testFree(int basePrice, int maxPrice, boolean isFree){
        //given
        Event event = Event.builder()
                .basePrice(basePrice)
                .maxPrice(maxPrice)
                .build();

        //when
        event.update();

        //then
        assertThat(event.isFree()).isEqualTo(isFree);
    }

    @Test
    void testOffline(){
        //given
        Event event = Event.builder()
                .location("강남역 네이버 D2 스타텁 팩토리")
                .build();

        //when
        event.update();

        //then
        assertThat(event.isOffline()).isTrue();

        //given
        event = Event.builder()
                .build();

        //when
        event.update();

        //then
        assertThat(event.isOffline()).isFalse();
    }

    private static Stream<Arguments> paramsForTestFree(){
        return Stream.of(
                Arguments.of(0,0,true),
                Arguments.of(100,0,false),
                Arguments.of(0,100,false),
                Arguments.of(100,200,false)
        );
    }

}