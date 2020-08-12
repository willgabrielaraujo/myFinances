package com.udemy.myfinances.api.resource;

import com.udemy.myfinances.api.dto.UpdateStatusDto;
import com.udemy.myfinances.api.dto.EntryDto;
import com.udemy.myfinances.exception.BusinessLogicException;
import com.udemy.myfinances.model.entity.Entry;
import com.udemy.myfinances.model.entity.User;
import com.udemy.myfinances.model.enums.EntryStatus;
import com.udemy.myfinances.model.enums.EntryType;
import com.udemy.myfinances.service.EntryService;
import com.udemy.myfinances.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/entries")
@RequiredArgsConstructor
public class EntryResource {
    private final EntryService service;
    private final UserService userService;

    @GetMapping
    public ResponseEntity search(@RequestParam(value = "description", required = false) String description,
                                 @RequestParam(value = "month", required = false) Integer month,
                                 @RequestParam(value = "year", required = false) Integer year,
                                 @RequestParam(value = "type", required = false) EntryType type,
                                 @RequestParam(value = "status", required = false) EntryStatus status,
                                 @RequestParam("user") Long userId) {

        try {
            Entry entryFilter = new Entry();
            entryFilter.setDescription(description);
            entryFilter.setMonth(month);
            entryFilter.setYear(year);
            entryFilter.setType(type);
            entryFilter.setStatus(status);

            User userFound = userService.findById(userId).orElseThrow(
                    () -> new BusinessLogicException("User not found."));

            entryFilter.setUser(userFound);
            List<Entry> entries = service.search(entryFilter);

            return ResponseEntity.ok(entries);
        } catch (BusinessLogicException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @GetMapping("{id}")
    public ResponseEntity<Entry> findEntry(@PathVariable("id") Long id) {
        return this.service.findById(id)
                .map(entry -> new ResponseEntity(converter(entry), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity save(@RequestBody EntryDto dto) {
        try {
            Entry entry = converterDTOToEntry(dto);
            Entry savedEntry = service.save(entry);
            return new ResponseEntity(savedEntry, HttpStatus.CREATED);
        } catch (BusinessLogicException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity update(@PathVariable("id") Long id, @RequestBody EntryDto dto) {
        return service.findById(id).map(entryFound -> {
            try {
                Entry convertedEntry = converterDTOToEntry(dto);
                convertedEntry.setId(entryFound.getId());
                Entry updatedEntry = service.update(convertedEntry);
                return ResponseEntity.ok(updatedEntry);
            } catch (BusinessLogicException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }).orElseGet(() -> new ResponseEntity("Entry not found.", HttpStatus.BAD_REQUEST));

    }

    @PutMapping("{id}/update-status")
    public ResponseEntity updateStatus(@PathVariable("id") Long id, @RequestBody UpdateStatusDto dto) {
        return service.findById(id).map(entry -> {
            EntryStatus status = EntryStatus.valueOf(dto.getStatus());

            if (status == null) {
                return ResponseEntity.badRequest().body("It was not possible to update status. Invalid status.");
            }

            try {
                Entry updatedEntry = service.updateStatus(entry, status);
                return ResponseEntity.ok(updatedEntry);
            } catch (BusinessLogicException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }).orElseGet(() -> new ResponseEntity("Entry not found.", HttpStatus.BAD_REQUEST));
    }

    @DeleteMapping("{id}")
    public ResponseEntity delete(@PathVariable("id") Long id) {
        return service.findById(id).map(entryFound -> {
            service.delete(entryFound);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }).orElseGet(() -> new ResponseEntity("Entry not found.", HttpStatus.BAD_REQUEST));
    }

    private EntryDto converter(Entry entry) {
        return EntryDto.builder()
                .id(entry.getId())
                .description(entry.getDescription())
                .year(entry.getYear())
                .month(entry.getMonth())
                .type(entry.getType().name())
                .status(entry.getStatus().name())
                .amount(entry.getAmount())
                .user(entry.getUser().getId())
                .build();
    }

    private Entry converterDTOToEntry(EntryDto dto) {
        Entry entry = new Entry();
        entry.setId(dto.getId());
        entry.setDescription(dto.getDescription());
        entry.setMonth(dto.getMonth());
        entry.setYear(dto.getYear());
        entry.setAmount(dto.getAmount());

        User user = userService.findById(dto.getUser())
                .orElseThrow(() -> new BusinessLogicException("User not found."));
        entry.setUser(user);

        if (dto.getType() != null) {
            entry.setType(EntryType.valueOf(dto.getType()));
        }

        if (dto.getStatus() != null) {
            entry.setStatus(EntryStatus.valueOf(dto.getStatus()));
        }

        return entry;
    }
}
