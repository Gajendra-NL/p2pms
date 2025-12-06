package com.gcloud.p2pms.patient_service.controller;

import com.gcloud.p2pms.patient_service.dto.PatientRequestDto;
import com.gcloud.p2pms.patient_service.dto.PatientResponseDto;
import com.gcloud.p2pms.patient_service.dto.validators.CreatePatientValidationGroup;
import com.gcloud.p2pms.patient_service.model.Patient;
import com.gcloud.p2pms.patient_service.repository.PatientRepository;
import com.gcloud.p2pms.patient_service.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/patients")
@Tag(name = "Patient", description = "API for managing patients")
public class PatientController {
    private PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping
    @Operation(summary = "Get Patients")
    public ResponseEntity<List<PatientResponseDto>> getPatients() {
        List<PatientResponseDto> patients = patientService.getPatients();
        return ResponseEntity.ok().body(patients);
    }

    @PostMapping
    @Operation(summary = "Create a new Patient")
    public ResponseEntity<PatientResponseDto> addPatient(
            @Validated({Default.class, CreatePatientValidationGroup.class})
            @RequestBody PatientRequestDto patientRequestDto
    ) {
        PatientResponseDto newPatient = patientService.createPatient(patientRequestDto);
        return ResponseEntity.ok().body(newPatient);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a patient")
    public ResponseEntity<PatientResponseDto> updatePatient(
            @PathVariable UUID id,
            @Validated({Default.class}) @RequestBody PatientRequestDto patientRequestDto
    ) {
       PatientResponseDto updatedPatient = patientService.updatePatient(id, patientRequestDto);
       return ResponseEntity.ok().body(updatedPatient);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Patient")
    public ResponseEntity<Void> deletePatient(@PathVariable UUID id) {
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }
}