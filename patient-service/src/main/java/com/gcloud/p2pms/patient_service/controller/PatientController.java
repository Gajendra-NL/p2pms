package com.gcloud.p2pms.patient_service.controller;

import com.gcloud.p2pms.patient_service.dto.PatientRequestDto;
import com.gcloud.p2pms.patient_service.dto.PatientResponseDto;
import com.gcloud.p2pms.patient_service.model.Patient;
import com.gcloud.p2pms.patient_service.service.PatientService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
@AllArgsConstructor
public class PatientController {
    private PatientService patientService;

    @GetMapping
    public ResponseEntity<List<PatientResponseDto>> getPatients() {
        List<PatientResponseDto> patients = patientService.getPatients();
        return ResponseEntity.ok().body(patients);
    }

    @PostMapping
    public ResponseEntity<PatientResponseDto> addPatient(@Valid @RequestBody PatientRequestDto patientRequestDto) {
        PatientResponseDto newPatient = patientService.createPatient(patientRequestDto);
        return ResponseEntity.ok().body(newPatient);
    }
}
