package com.hortonworks.se.healthcare.dto;

public class Patient {

	public String patientId;
	public String eventTime;
	public Double heartRate;
	public Double respitatoryRate;
	public Double oxygenRate;
	public Double diastolicBloodPressure;
	public Double systolicBloodPressure;
	@Override
	public String toString() {
		return "Patient [PATIENT_ID=" + patientId + ", eventTime=" + eventTime
				+ ", heartRate=" + heartRate + ", respitatoryRate="
				+ respitatoryRate + ", oxygenRate=" + oxygenRate
				+ ", diastolicBloodPressure=" + diastolicBloodPressure
				+ ", systolicBloodPressure=" + systolicBloodPressure + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((patientId == null) ? 0 : patientId.hashCode());
		result = prime
				* result
				+ ((diastolicBloodPressure == null) ? 0
						: diastolicBloodPressure.hashCode());
		result = prime * result
				+ ((eventTime == null) ? 0 : eventTime.hashCode());
		result = prime * result
				+ ((heartRate == null) ? 0 : heartRate.hashCode());
		result = prime * result
				+ ((oxygenRate == null) ? 0 : oxygenRate.hashCode());
		result = prime * result
				+ ((respitatoryRate == null) ? 0 : respitatoryRate.hashCode());
		result = prime
				* result
				+ ((systolicBloodPressure == null) ? 0 : systolicBloodPressure
						.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Patient other = (Patient) obj;
		if (patientId == null) {
			if (other.patientId != null)
				return false;
		} else if (!patientId.equals(other.patientId))
			return false;
		if (diastolicBloodPressure == null) {
			if (other.diastolicBloodPressure != null)
				return false;
		} else if (!diastolicBloodPressure.equals(other.diastolicBloodPressure))
			return false;
		if (eventTime == null) {
			if (other.eventTime != null)
				return false;
		} else if (!eventTime.equals(other.eventTime))
			return false;
		if (heartRate == null) {
			if (other.heartRate != null)
				return false;
		} else if (!heartRate.equals(other.heartRate))
			return false;
		if (oxygenRate == null) {
			if (other.oxygenRate != null)
				return false;
		} else if (!oxygenRate.equals(other.oxygenRate))
			return false;
		if (respitatoryRate == null) {
			if (other.respitatoryRate != null)
				return false;
		} else if (!respitatoryRate.equals(other.respitatoryRate))
			return false;
		if (systolicBloodPressure == null) {
			if (other.systolicBloodPressure != null)
				return false;
		} else if (!systolicBloodPressure.equals(other.systolicBloodPressure))
			return false;
		return true;
	}

}
