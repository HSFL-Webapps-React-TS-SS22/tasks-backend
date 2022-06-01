{{/*
Expand the name of the chart.
*/}}
{{- define "tasks-backend.name" -}}
{{- default .Chart.Name .Values.nameOverride | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Create a default fully qualified app name.
We truncate at 63 chars because some Kubernetes name fields are limited to this (by the DNS naming spec).
If release name contains chart name it will be used as a full name.
*/}}
{{- define "tasks-backend.fullname" -}}
{{- if .Values.fullnameOverride }}
{{- .Values.fullnameOverride | trunc 63 | trimSuffix "-" }}
{{- else }}
{{- $name := default .Chart.Name .Values.nameOverride }}
{{- if contains $name .Release.Name }}
{{- .Release.Name | trunc 63 | trimSuffix "-" }}
{{- else }}
{{- printf "%s-%s" .Release.Name $name | trunc 63 | trimSuffix "-" }}
{{- end }}
{{- end }}
{{- end }}

{{- define "tasks-backend.backend.fullname" -}}
{{- printf "%s-backend" (include "tasks-backend.fullname" .) }}
{{- end }}

{{- define "tasks-backend.mongodb.fullname" -}}
{{- printf "%s-mongodb" (include "tasks-backend.fullname" .) }}
{{- end }}

{{/*
Create chart name and version as used by the chart label.
*/}}
{{- define "tasks-backend.chart" -}}
{{- printf "%s-%s" .Chart.Name .Chart.Version | replace "+" "_" | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Common labels
*/}}
{{- define "tasks-backend.labels" -}}
helm.sh/chart: {{ include "tasks-backend.chart" . }}
{{ include "tasks-backend.selectorLabels" . }}
{{- if .Chart.AppVersion }}
app.kubernetes.io/version: {{ .Chart.AppVersion | quote }}
{{- end }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
{{- end }}

{{- define "tasks-backend.keycloak.labels" -}}
{{ include "tasks-backend.labels" . }}
component: keycloak
{{- end }}

{{- define "tasks-backend.backend.labels" -}}
{{ include "tasks-backend.labels" . }}
component: backend
{{- end }}

{{- define "tasks-backend.frontend.labels" -}}
{{ include "tasks-backend.labels" . }}
component: frontend
{{- end }}

{{/*
Selector labels
*/}}
{{- define "tasks-backend.selectorLabels" -}}
app.kubernetes.io/name: {{ include "tasks-backend.name" . }}
app.kubernetes.io/instance: {{ .Release.Name }}
{{- end }}

{{- define "tasks-backend.keycloak.selectorLabels" -}}
{{ include "tasks-backend.selectorLabels" . }}
component: keycloak
{{- end }}

{{- define "tasks-backend.frontend.selectorLabels" -}}
{{ include "tasks-backend.selectorLabels" . }}
component: frontend
{{- end }}

{{- define "tasks-backend.backend.selectorLabels" -}}
{{ include "tasks-backend.selectorLabels" . }}
component: backend
{{- end }}

{{/*
Create the name of the service account to use
*/}}
{{- define "tasks-backend.serviceAccountName" -}}
{{- if .Values.serviceAccount.create }}
{{- default (include "tasks-backend.fullname" .) .Values.serviceAccount.name }}
{{- else }}
{{- default "default" .Values.serviceAccount.name }}
{{- end }}
{{- end }}

{{- define "keycloak.postgresql.fullname" -}}
{{- printf "%s-%s" .Release.Name "postgresql" | trunc 63 | trimSuffix "-" -}}
{{- end -}}