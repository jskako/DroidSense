package utils

const val NUMBER_REGEX = "\\d+$"

private const val IP_REGEX = ("(\\d{1,2}|(0|1)\\"
        + "d{2}|2[0-4]\\d|25[0-5])")

const val ipRegex = (IP_REGEX + "\\."
        + IP_REGEX + "\\."
        + IP_REGEX + "\\."
        + IP_REGEX)
