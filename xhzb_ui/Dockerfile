FROM node:18.20.2 as build
COPY . /app
WORKDIR /app
ARG PUBLIC_PATH=/
ARG MODE
RUN npm -v
RUN npm install  && npm run build:prod
RUN ls -la /app/dist/

FROM nginx:alpine
ARG PUBLIC_PATH=/
COPY --from=build /app/dist/ /usr/share/nginx/html/
EXPOSE 80