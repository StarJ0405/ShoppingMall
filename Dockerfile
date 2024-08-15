FROM ghdtjdwo126/ubuntu-preset AS builder
WORKDIR /frontend
COPY /frontend/*.json /frontend/
RUN npm install
COPY /frontend/public /frontend/
COPY /frontend/src/. /frontend/src/
COPY /frontend/*.mjs /frontend/
COPY /frontend/*.ts /frontend/
RUN npm run build
WORKDIR /backend
COPY ./backend/src/. /backend/src/
COPY ./backend/gradlew /backend/
COPY ./backend/build.gradle /backend/
COPY ./backend/gradle/. /backend/gradle/
COPY ./backend/settings.gradle /backend/
RUN chmod +x ./gradlew
RUN ./gradlew bootJar

FROM ghdtjdwo126/ubuntu-preset
WORKDIR /
COPY --from=builder /frontend/.next/. /frontend/.next/
COPY --from=builder /backend/build/libs/*.jar /backend/
COPY /backend/run.sh /backend/
COPY ./frontend/run.sh /frontend/
COPY ./frontend/public/. /frontend/public/.
COPY ./frontend/package*.json /frontend/
COPY run.sh run.sh
RUN chmod +x ./run.sh
RUN chmod +x ./backend/run.sh
RUN chmod +x ./frontend/run.sh
WORKDIR /frontend
RUN npm i
WORKDIR /
CMD ["/run.sh"]
