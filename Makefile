IMG_NS?=MUST_SPECIFIED
IMG_TAG?=latest
SERVICE= xxx
IMG=xxx

image="docker.hub.com/$(IMG_NS)/$(IMG):$(IMG_TAG)"

all: image push

image:
	@echo building $(image) ...
	@docker build -t $(image) --build-arg SERVICE=$(SERVICE) .

push:
	@echo pushing $(image) ...
	@docker push $(image)
