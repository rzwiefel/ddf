(ns zwiefs.entry
  (gen-class
    :name zwiefs.entry.Point
    :implements [java.io.Serializable]
    :init init
    :state state
    :prefix "point-"
    :constructors {[] []}
    :methods [[start [] void]
              [destroy [] void]]))

(defn point-init
  []
  [[]])

(defn point-start [this]
  (println "Hello, we're starting up now!"))

(defn point-destroy [this]
  (println "Goodbye, we're shutting down now!"))
