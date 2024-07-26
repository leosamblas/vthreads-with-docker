#!/bin/bash

# Variables
DOCKER_IMAGE_NAME="vthreads"
DOCKER_IMAGE_TAG="1.0.0"
DOCKERFILE_PATH="./Dockerfile"
K8S_DEPLOYMENT_FILE="deployment.yaml"
K8S_SERVICE_FILE="service.yaml"
K8S_INGRESS_FILE="ingress.yaml"
K8S_NAMESPACE="development"

# Step 1: Build the Docker image only locally
echo "Building Docker image..."
docker build -t ${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG} -f ${DOCKERFILE_PATH} .

cd "$(dirname "$0")"/k8s

# Step 2: Apply the Kubernetes namespace (if not already created)
echo "Creating Kubernetes namespace..."
kubectl create namespace ${K8S_NAMESPACE} || true

# Step 3: Apply the Kubernetes Deployment
echo "Applying Kubernetes Deployment..."
kubectl apply -f ${K8S_DEPLOYMENT_FILE} -n ${K8S_NAMESPACE}

# Step 4: Apply the Kubernetes Service
echo "Applying Kubernetes Service..."
kubectl apply -f ${K8S_SERVICE_FILE} -n ${K8S_NAMESPACE}

# Step 5: Apply the Kubernetes Ingress
echo "Applying Kubernetes Ingress..."
kubectl apply -f ${K8S_INGRESS_FILE} -n ${K8S_NAMESPACE}

echo "Deployment complete."