package com.gcloud.p2pms.patient_service.service;

import com.gcloud.p2pms.patient_service.dto.PatientRequestDto;
import com.gcloud.p2pms.patient_service.dto.PatientResponseDto;
import com.gcloud.p2pms.patient_service.exception.EmailAlreadyExistsException;
import com.gcloud.p2pms.patient_service.mapper.PatientMapper;
import com.gcloud.p2pms.patient_service.model.Patient;
import com.gcloud.p2pms.patient_service.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {
    private PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public List<PatientResponseDto> getPatients() {
        List<Patient> patients = patientRepository.findAll();
//        List<PatientResponseDto> patientsResponseDTOs = patients.stream().map(patient -> PatientMapper.toDTO(patient)).toList();
//        return patientsResponseDTOs;
        return patients.stream().map(PatientMapper::toDTO).toList();
    }

    public PatientResponseDto createPatient(PatientRequestDto patientRequestDto) {
        if(patientRepository.existsByEmail(patientRequestDto.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists" + patientRequestDto.getEmail());
        }

        Patient newPatient = patientRepository.save(PatientMapper.toModel(patientRequestDto));
        return PatientMapper.toDTO(newPatient);
    }
}
