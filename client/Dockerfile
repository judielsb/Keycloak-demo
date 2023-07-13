FROM node:18-alpine


WORKDIR /client
COPY package.json /client
COPY package-lock.json /client


RUN npm install
COPY . /client

CMD ["npm", "run", "dev"]