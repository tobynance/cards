(ns cards.clientproxy
  (:require [clojure.tools.logging :as log]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defprotocol process-protocol
  (process-read-line [process])
  (process-write-line [process text])
  (process-close [process]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defrecord ClientProxy [name ^java.lang.Process process ^java.io.BufferedReader in ^java.io.BufferedWriter out cards score]
  process-protocol
  (process-read-line
    [p]
    (.readLine in))
  (process-write-line
    [p text]
    (.write out (.trim text))
    (.write out "\n")
    (.flush out))
  (process-close
    [p]
    (.destroy process)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn start-client-proxy
  "Executes a command-line program, returning a stdout stream and a
   stdin stream.  Takes a list of strings which represent the command
   arguments"
  [name args ^java.io.File dir]
  (let [process (.exec (Runtime/getRuntime) (reduce str (interleave args (iterate str " "))) nil dir)
        in (java.io.BufferedReader. (java.io.InputStreamReader. (.getInputStream process) (java.nio.charset.Charset/forName "UTF-8")))
        out (java.io.BufferedWriter. (java.io.OutputStreamWriter. (.getOutputStream process) (java.nio.charset.Charset/forName "UTF-8")))]
    (ClientProxy. name process in out [] 0)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn clients-in-order
  "Get the list of clients in play order"
  [game-server]
  (map #(get (:clients @game-server) %) (:client-name-order @game-server)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn broadcast-message
  "Send a message to every client"
  [clients message]
  (log/info "Broadcasting message: " message)
  (doseq [client clients]
    (process-write-line client message)))
