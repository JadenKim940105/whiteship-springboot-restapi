package me.summerbell.whiteshipspringbootrestapi.events;

import lombok.RequiredArgsConstructor;
import me.summerbell.whiteshipspringbootrestapi.accounts.Account;
import me.summerbell.whiteshipspringbootrestapi.accounts.AccountAdaptor;
import me.summerbell.whiteshipspringbootrestapi.accounts.CurrentUser;
import me.summerbell.whiteshipspringbootrestapi.commons.ErrorsResource;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;


@RestController
@RequestMapping(value = "/api/events", produces = MediaTypes.HAL_JSON_VALUE+";charset=UTF-8")
@RequiredArgsConstructor
public class EventsController {

    private final EventRepository eventRepository;
    private final ModelMapper modelMapper;
    private final EventValidator eventValidator;

    @PostMapping
    public ResponseEntity createEvent(@RequestBody @Valid EventDto eventDto,
                                      Errors errors,
                                      @CurrentUser Account account){


        if(errors.hasErrors()){
            return ResponseEntity.badRequest().body(new ErrorsResource(errors));
        }

        eventValidator.validate(eventDto, errors);
        if(errors.hasErrors()){
            return ResponseEntity.badRequest().body(new ErrorsResource(errors));
        }

        Event event = modelMapper.map(eventDto, Event.class);
        event.update();
        event.setManager(account);


        Event newEvent = eventRepository.save(event);

        WebMvcLinkBuilder selfLinkBuilder = linkTo(EventsController.class).slash(newEvent.getId());
        URI createdUri = selfLinkBuilder.toUri();
        EventResource eventResource = new EventResource(event);
        eventResource.add(linkTo(EventsController.class).withRel("query-events"));
        eventResource.add(selfLinkBuilder.withRel("update-event"));
        eventResource.add(Link.of("/docs/index.html#resources-events-create").withRel("profile"));
        return ResponseEntity.created(createdUri).body(eventResource);
    }

    @GetMapping
    public ResponseEntity queryEvents(Pageable pageable,
                                      PagedResourcesAssembler<Event> assembler,
                                      @CurrentUser Account account){

        Page<Event> page = eventRepository.findAll(pageable);
        var pagedResource = assembler.toModel(page, e -> new EventResource(e));
        pagedResource.add(Link.of("/docs/index.html#resources-events-list").withRel("profile"));
         if (account != null){
            pagedResource.add(linkTo(EventsController.class).withRel("create-event"));
        }
        return ResponseEntity.ok().body(pagedResource);
    }

    @GetMapping("/{id}")
    public ResponseEntity getEvent(@PathVariable Integer id,
                                   @CurrentUser Account account){
        Optional<Event> optionalEvent = eventRepository.findById(id);
        if (optionalEvent.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        Event event = optionalEvent.get();
        EntityModel<Event> eventResource = new EventResource(event);
        eventResource.add(Link.of("/docs/index.html#resources-events-get").withRel("profile"));
        if(event.getManager().equals(account)){
            eventResource.add(linkTo(EventsController.class).slash(event.getId()).withRel("update-event"));
        }
        return ResponseEntity.ok(eventResource);
    }

    @PutMapping("/{id}")
    public ResponseEntity modifyEvent(@PathVariable Integer id,
                                      @RequestBody @Valid EventDto eventDto,
                                      Errors errors,
                                      @CurrentUser Account account){

        Optional<Event> optionalEvent = eventRepository.findById(id);

        if (optionalEvent.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        if (errors.hasErrors()){
            return ResponseEntity.badRequest().body(new ErrorsResource(errors));
        }

        eventValidator.validate(eventDto, errors);
        if (errors.hasErrors()){
            return ResponseEntity.badRequest().body(new ErrorsResource(errors));
        }


        Event existingEvent = optionalEvent.get();
        if(!existingEvent.getManager().equals(account)){
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }

        modelMapper.map(eventDto, existingEvent);
        Event savedEvent = eventRepository.save(existingEvent);
        EventResource eventResource = new EventResource(savedEvent);
        eventResource.add(Link.of("/docs/index.html#resource-events-update").withRel("profile"));

        return ResponseEntity.ok(eventResource);
    }
}
