--subinfos constraint
ALTER TABLE info ADD CONSTRAINT not_more_than_one_specific_info CHECK ((info_difr_id IS NULL AND info_test_id IS NULL) OR (info_difr_id IS NULL AND info_test_id IS NOT NULL) OR (info_difr_id IS NOT NULL AND info_test_id IS NULL));